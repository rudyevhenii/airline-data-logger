package com.diploma.airline_data_logger.repository.impl;

import com.diploma.airline_data_logger.constants.ProjectConstants;
import com.diploma.airline_data_logger.constants.TriggerOperation;
import com.diploma.airline_data_logger.dto.TableSchemaDto;
import com.diploma.airline_data_logger.repository.TableAuditRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Repository
public class TableAuditRepositoryImpl implements TableAuditRepository {

    private final JdbcTemplate jdbcTemplate;
    private String auditTable;

    public TableAuditRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<String> getAllTableNames() {
        List<TableSchemaDto> tableSchemas = new ArrayList<>();
        String sql = """
                SELECT table_name
                FROM information_schema.TABLES
                WHERE table_schema = '%s'
                AND table_name <> 'employees'
                AND NOT table_name LIKE 'audit_%%';
                """.formatted(ProjectConstants.DB_SCHEMA_NAME);

        return jdbcTemplate.queryForList(sql, String.class);
    }

    @Override
    public List<String> getAllColumnsForTable(String tableName) {
        String sql = """
                SELECT column_name
                FROM information_schema.COLUMNS
                WHERE table_name = ?
                AND table_schema = '%s'
                ORDER BY ordinal_position;
                """.formatted(ProjectConstants.DB_SCHEMA_NAME);

        return jdbcTemplate.queryForList(sql, String.class, tableName);
    }

    @Override
    public List<String> getAllColumnsDataType(String tableName) {
        String sql = """
                SELECT data_type
                FROM information_schema.COLUMNS
                WHERE table_name = ?
                AND table_schema = '%s'
                ORDER BY ordinal_position;
                """.formatted(ProjectConstants.DB_SCHEMA_NAME);

        return jdbcTemplate.queryForList(sql, String.class, tableName);
    }

    @Override
    public void createAuditTableByTableName(String tableName) {
        String columnsBeforeChange = getColumnsStructure(tableName, "");
        String columnsAfterChange = getColumnsStructure(tableName, "_");
        String completeColumnsStructure = columnsBeforeChange + columnsAfterChange;

        String auditTable = "audit_" + tableName;
        String sql = """
                CREATE TABLE IF NOT EXISTS %s (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    date_op DATETIME NOT NULL,
                    code_op CHAR(1) NOT NULL,
                    user_op VARCHAR(100) NOT NULL,
                    host_op VARCHAR(100) NOT NULL,
                %s
                );""".formatted(auditTable, completeColumnsStructure);

        jdbcTemplate.execute(sql);
    }

    private String getColumnsStructure(String tableName, String suffix) {
        List<String> columnNames = getAllColumnsForTable(tableName);
        List<String> columnDataTypes = getAllColumnsDataType(tableName);

        boolean applyDelimiter = suffix.equals("_");
        int columnLength = columnNames.size();

        return IntStream.range(0, columnLength)
                .filter(i -> !(tableName.substring(0, tableName.length() - 1) + "_id_")
                        .equals(columnNames.get(i) + suffix))
                .mapToObj(i -> "\t%s %s%s%s".formatted(columnNames.get(i) + suffix,
                        columnDataTypes.get(i),
                        columnDataTypes.get(i).equalsIgnoreCase("varchar") ? "(100)" : "",
                        applyDelimiter ? "" : ",\n"))
                .collect(Collectors.joining(!applyDelimiter ? "" : ",\n"));
    }

    @Override
    public boolean doesAuditTableExist(String tableName) {
        String auditTable = "audit_" + tableName;
        String sql = """
                SELECT COUNT(*)
                FROM information_schema.TABLES
                WHERE table_name = ?
                AND table_schema = '%s';
                """.formatted(ProjectConstants.DB_SCHEMA_NAME);

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, auditTable);
        return count > 0;
    }

    @Override
    public void deleteAuditTableByTableName(String tableName) {
        String auditTable = "audit_" + tableName;
        String sql = "DROP TABLE IF EXISTS %s".formatted(auditTable);

        jdbcTemplate.execute(sql);
    }

    @Override
    public void createTriggersForTable(String tableName) {
        String auditTable = "audit_" + tableName;

        for (TriggerOperation triggerOperation : TriggerOperation.values()) {
            String trigger = createTriggerByTableName(tableName, auditTable, triggerOperation);

            jdbcTemplate.execute(trigger);
        }
    }

    private String createTriggerByTableName(String tableName, String auditTable,
                                            TriggerOperation triggerOperation) {
        List<String> auditTableNames = getAllColumnsForTable(auditTable);
        List<String> tableNames = getAllColumnsForTable(tableName);

        String trigger = """
                CREATE TRIGGER after_%s_%s
                AFTER %s ON %s
                FOR EACH ROW
                BEGIN
                    INSERT INTO %s (
                        %s%s
                    ) VALUES (
                        NOW(), '%c', USER(), @@hostname, %s
                    );
                END;""".formatted(
                triggerOperation.getOperationNameLowerCase(),
                tableName.endsWith("s") ? tableName.substring(0, tableName.length() - 1) : tableName,
                triggerOperation.getOperationNameUpperCase(),
                tableName,
                auditTable,
                auditTableNames.subList(1, 6).stream()
                        .map(col -> col + ", ")
                        .collect(Collectors.joining()),
                getAuditColumnsByTrigger(auditTableNames, tableNames, triggerOperation),
                triggerOperation.getOperationNameUpperCase().charAt(0),
                getValueByTrigger(tableNames, triggerOperation));

        return trigger;
    }

    private String getAuditColumnsByTrigger(List<String> auditTableNames,
                                            List<String> tableNames, TriggerOperation triggerOperation) {
        return switch (triggerOperation) {
            case INSERT -> String.join(", ", auditTableNames.subList(
                    auditTableNames.size() - tableNames.size() + 1, auditTableNames.size()));
            case UPDATE -> String.join(", ",
                    auditTableNames.subList(6, auditTableNames.size()));
            case DELETE -> String.join(", ",
                    auditTableNames.subList(6, auditTableNames.size() - tableNames.size() + 1));
        };
    }

    private String getValueByTrigger(List<String> tableNames, TriggerOperation triggerOperation) {
        return switch (triggerOperation) {
            case INSERT -> tableNames.stream()
                    .map(col -> "NEW." + col)
                    .collect(Collectors.joining(", "));
            case UPDATE -> tableNames.stream()
                    .map(col -> "OLD." + col + ", ")
                    .collect(Collectors.joining()) +
                    tableNames.stream()
                            .filter(col -> !("NEW." + tableNames.get(0)).equals("NEW." + col))
                            .map(col -> "NEW." + col)
                            .collect(Collectors.joining(", "));
            case DELETE -> tableNames.stream()
                    .map(col -> "OLD." + col)
                    .collect(Collectors.joining(", "));
        };
    }

    @Override
    public boolean doTriggersExistForTable(String tableName) {
        String sql = """
                SELECT COUNT(*)
                FROM information_schema.TRIGGERS
                WHERE EVENT_OBJECT_TABLE = ?
                AND TRIGGER_SCHEMA = '%s';
                """.formatted(ProjectConstants.DB_SCHEMA_NAME);

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName);
        return count > 0;
    }

    @Override
    public void deleteTriggersByTableName(String tableName) {
        for (TriggerOperation trigger : TriggerOperation.values()) {
            String deleteTrigger = """
                DROP TRIGGER IF EXISTS `%s`.`%s`;
                """.formatted(ProjectConstants.DB_SCHEMA_NAME,
                    "after_%s_%s".formatted(trigger.getOperationNameLowerCase(),
                    tableName.substring(0, tableName.length() - 1)));

            jdbcTemplate.execute(deleteTrigger);
        }
    }
}
