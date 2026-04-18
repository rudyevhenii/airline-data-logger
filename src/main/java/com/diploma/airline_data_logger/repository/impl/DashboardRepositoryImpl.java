package com.diploma.airline_data_logger.repository.impl;

import com.diploma.airline_data_logger.dto.TableAuditDto;
import com.diploma.airline_data_logger.dto.TableSchemaDto;
import com.diploma.airline_data_logger.mapper.TableAutidDtoRowMapper;
import com.diploma.airline_data_logger.repository.DashboardRepository;
import com.diploma.airline_data_logger.repository.TableMetadataProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.diploma.airline_data_logger.query.SqlQuery.SELECT_ALL_WHERE_SQL;
import static com.diploma.airline_data_logger.query.SqlQuery.UPSERT_RECORD_SQL;

@Repository
@RequiredArgsConstructor
public class DashboardRepositoryImpl implements DashboardRepository {

    private static final String AUDIT_TABLE_PREFIX = "audit_";

    private final JdbcTemplate jdbcTemplate;
    private final TableMetadataProvider tableMetadataProvider;

    @Override
    public List<TableSchemaDto> getAllTableSchemas() {
        List<String> tableNames = tableMetadataProvider.getAllTableNames();

        return tableNames.stream()
                .map(this::getTableSchemaDto)
                .toList();
    }

    @Override
    public List<String> getAllAuditTableColumns(String tableName) {
        return tableMetadataProvider.getAllColumnsForTable(AUDIT_TABLE_PREFIX + tableName);
    }

    @Override
    public List<TableAuditDto> loadDataFromAuditTable(String tableName,
                                                      String startTime, String endTime) {
        String auditTable = AUDIT_TABLE_PREFIX + tableName;
        String sql = getDateFiltrationSql(auditTable, startTime, endTime);

        return jdbcTemplate.query(sql, new TableAutidDtoRowMapper(tableMetadataProvider, tableName, auditTable));
    }

    @Override
    public boolean doesAuditTableExist(String tableName) {
        String auditTable = AUDIT_TABLE_PREFIX + tableName;

        return tableMetadataProvider.doesTableExist(auditTable);
    }

    @Override
    public boolean restoreRecord(String tableName, int id) {
        List<String> tableColumnNames = tableMetadataProvider.getAllColumnsForTable(tableName);
        TableAuditDto tableAudit = findRecordInAuditTableById(tableName, id)
                .orElseThrow(() -> new IllegalStateException("Couldn't find a record with id: " + id));
        List<String> insertValues = tableAudit.getColumnsBeforeChange();

        String insertPlaceholders = getInsertPlaceHolders(tableAudit.getColumnsBeforeChange());
        String valueAlias = getValueAlias(tableName);
        String updateStructure = getUpdateStructure(tableColumnNames, valueAlias);

        String sql = UPSERT_RECORD_SQL.formatted(tableName,
                String.join(", ", tableColumnNames),
                tableAudit.getTableId(),
                insertPlaceholders,
                valueAlias,
                updateStructure);

        int count = jdbcTemplate.update(sql, insertValues.toArray());
        return count > 0;
    }

    @Override
    public Optional<TableAuditDto> findRecordInAuditTableById(String tableName, int id) {
        String auditTable = AUDIT_TABLE_PREFIX + tableName;
        String sql = SELECT_ALL_WHERE_SQL.formatted(auditTable, id);

        TableAuditDto tableAuditDto = jdbcTemplate.queryForObject(sql,
                new TableAutidDtoRowMapper(tableMetadataProvider, tableName, auditTable));

        return Optional.ofNullable(tableAuditDto);
    }

    private TableSchemaDto getTableSchemaDto(String tableName) {
        return new TableSchemaDto(
                tableName,
                tableMetadataProvider.getAllColumnsForTable(tableName),
                tableMetadataProvider.doesTableExist(AUDIT_TABLE_PREFIX + tableName),
                tableMetadataProvider.doTriggersExistForTable(tableName));
    }

    private String getDateFiltrationSql(String auditTable, String startTime, String endTime) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM %s ".formatted(auditTable));

        if (!(startTime.isEmpty() || endTime.isEmpty())) {
            sqlBuilder.append("WHERE date_op BETWEEN '%s' AND '%s' ".formatted(startTime, endTime));
        }
        if (!startTime.isEmpty() && endTime.isEmpty()) {
            sqlBuilder.append("WHERE date_op >= '%s' ".formatted(startTime));
        }
        if (!endTime.isEmpty() && startTime.isEmpty()) {
            sqlBuilder.append("WHERE date_op <= '%s' ".formatted(endTime));
        }
        sqlBuilder.append("ORDER BY date_op DESC;");

        return sqlBuilder.toString();
    }

    private String getUpdateStructure(List<String> tableColumnNames, String valueAlias) {
        return tableColumnNames.subList(1, tableColumnNames.size()).stream()
                .map(col -> "\t%1$s = %2$s.%1$s".formatted(col, valueAlias))
                .collect(Collectors.joining(",\n"));
    }

    private String getValueAlias(String tableName) {
        return "new_" + (tableName.endsWith("s") ?
                tableName.substring(0, tableName.length() - 1) : tableName);
    }

    private String getInsertPlaceHolders(List<String> insertValues) {
        return insertValues.stream()
                .map(val -> "?")
                .collect(Collectors.joining(", "));
    }

}
