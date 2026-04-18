package com.diploma.airline_data_logger.repository.impl;

import com.diploma.airline_data_logger.constants.TriggerOperation;
import com.diploma.airline_data_logger.repository.TableAuditRepository;
import com.diploma.airline_data_logger.repository.TableMetadataProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.diploma.airline_data_logger.query.SqlQuery.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TableAuditRepositoryImpl implements TableAuditRepository {

    private static final String AUDIT_TABLE_PREFIX = "audit_";

    private final JdbcTemplate jdbcTemplate;
    private final TableMetadataProvider tableMetadataProvider;

    @Value("${application.database.name}")
    private String databaseName;

    @Override
    public void createAuditTable(String tableName) {
        List<String> columnNames = tableMetadataProvider.getAllColumnsForTable(tableName);
        List<String> columnDataTypes = tableMetadataProvider.getAllColumnsDataType(tableName);

        String columnsBeforeChange = getColumnsStructure(columnNames, columnDataTypes, tableName, "");
        String columnsAfterChange = getColumnsStructure(columnNames, columnDataTypes, tableName, "_");
        String sql = createAuditTableSql(tableName, columnsBeforeChange, columnsAfterChange);

        jdbcTemplate.execute(sql);
    }

    private String createAuditTableSql(String tableName, String columnsBeforeChange, String columnsAfterChange) {
        String completeColumnsStructure = columnsBeforeChange + columnsAfterChange;

        return CREATE_TABLE_IF_NOT_EXISTS_SQL.formatted(AUDIT_TABLE_PREFIX + tableName, completeColumnsStructure);
    }

    private String getColumnsStructure(List<String> columnNames, List<String> columnDataTypes,
                                       String tableName, String suffix) {
        boolean applyDelimiter = suffix.equals("_");

        return IntStream.range(0, columnNames.size())
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
        String sql = DROP_TABLE_IF_EXISTS_SQL.formatted(AUDIT_TABLE_PREFIX + tableName);

        jdbcTemplate.execute(sql);
    }

    @Override
    public void createTriggersForTable(String tableName) {
        for (TriggerOperation triggerOperation : TriggerOperation.values()) {
            String trigger = createTriggerByTableName(tableName, AUDIT_TABLE_PREFIX + tableName,
                    triggerOperation);

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

        String trigger = CREATE_TRIGGER_SQL.formatted(
                triggerOperation.getOperationNameLowerCase(), name, triggerOperation.getOperationNameUpperCase(),
                tableName, auditTable, auditColumns, auditColumnsByTrigger,
                triggerOperation.getOperationNameUpperCase().charAt(0), valueByTrigger);

        log.info("Created trigger for {} operation: {}", triggerOperation, trigger);
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
            String deleteTrigger = DROP_TRIGGER_IF_EXISTS.formatted(databaseName,
                    "after_%s_%s".formatted(trigger.getOperationNameLowerCase(),
                            tableName.endsWith("s") ? tableName.substring(0, tableName.length() - 1) : tableName));

            jdbcTemplate.execute(deleteTrigger);
        }
    }

}
