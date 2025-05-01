package com.diploma.airline_data_logger.repository.impl;

import com.diploma.airline_data_logger.constants.ProjectConstants;
import com.diploma.airline_data_logger.constants.TriggerOperation;
import com.diploma.airline_data_logger.repository.TableAuditRepository;
import com.diploma.airline_data_logger.repository.TableMetadataProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Repository
public class TableAuditRepositoryImpl implements TableAuditRepository {

    private final JdbcTemplate jdbcTemplate;
    private final TableMetadataProvider tableMetadataProvider;

    public TableAuditRepositoryImpl(DataSource dataSource,
                                    TableMetadataProvider tableMetadataProvider) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.tableMetadataProvider = tableMetadataProvider;
    }

    @Override
    public void createAuditTable(String tableName) {
        String columnsBeforeChange = getColumnsStructure(tableName, "");
        String columnsAfterChange = getColumnsStructure(tableName, "_");
        String completeColumnsStructure = columnsBeforeChange + columnsAfterChange;

        String auditTable = "audit_" + tableName;
        String sql = """
                CREATE TABLE IF NOT EXISTS %s (
                    audit_id INT AUTO_INCREMENT PRIMARY KEY,
                    date_op DATETIME NOT NULL,
                    code_op CHAR(1) NOT NULL,
                    user_op VARCHAR(100) NOT NULL,
                    host_op VARCHAR(100) NOT NULL,
                %s
                );""".formatted(auditTable, completeColumnsStructure);

        jdbcTemplate.execute(sql);
    }

    private String getColumnsStructure(String tableName, String suffix) {
        List<String> columnNames = tableMetadataProvider.getAllColumnsForTable(tableName);
        List<String> columnDataTypes = tableMetadataProvider.getAllColumnsDataType(tableName);

        boolean applyDelimiter = suffix.equals("_");
        int columnLength = columnNames.size();

        return IntStream.range(0, columnLength)
                .filter(i -> tableName.endsWith("s") ? !(tableName.substring(0, tableName.length() - 1) + "_id_")
                                .equals(columnNames.get(i) + suffix) :
                        !(tableName + "_id_").equals(columnNames.get(i) + suffix))
                .mapToObj(i -> "\t%s %s%s%s".formatted(columnNames.get(i) + suffix,
                        columnDataTypes.get(i),
                        columnDataTypes.get(i).equalsIgnoreCase("varchar") ? "(100)" : "",
                        applyDelimiter ? "" : ",\n"))
                .collect(Collectors.joining(!applyDelimiter ? "" : ",\n"));
    }

    @Override
    public void deleteAuditTable(String tableName) {
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
        List<String> auditTableNames = tableMetadataProvider.getAllColumnsForTable(auditTable);
        List<String> tableNames = tableMetadataProvider.getAllColumnsForTable(tableName);

        String name = tableName.endsWith("s") ? tableName.substring(0, tableName.length() - 1) : tableName;
        String auditColumns = auditTableNames.subList(1, 6).stream()
                .map(col -> col + ", ")
                .collect(Collectors.joining());
        String auditColumnsByTrigger = getAuditColumnsByTrigger(auditTableNames, tableNames, triggerOperation);
        String valueByTrigger = getValueByTrigger(tableNames, triggerOperation);

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
                name,
                triggerOperation.getOperationNameUpperCase(),
                tableName,
                auditTable,
                auditColumns,
                auditColumnsByTrigger,
                triggerOperation.getOperationNameUpperCase().charAt(0),
                valueByTrigger);

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
    public void deleteTriggersForTable(String tableName) {
        for (TriggerOperation trigger : TriggerOperation.values()) {
            String deleteTrigger = """
                    DROP TRIGGER IF EXISTS `%s`.`%s`;
                    """.formatted(ProjectConstants.DB_SCHEMA_NAME,
                    "after_%s_%s".formatted(trigger.getOperationNameLowerCase(),
                            tableName.endsWith("s") ? tableName.substring(0, tableName.length() - 1) : tableName));

            jdbcTemplate.execute(deleteTrigger);
        }
    }

}
