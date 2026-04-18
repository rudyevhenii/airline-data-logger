package com.diploma.airline_data_logger.mapper;

import com.diploma.airline_data_logger.dto.TableAuditDto;
import com.diploma.airline_data_logger.repository.TableMetadataProvider;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TableAutidDtoRowMapper implements RowMapper<TableAuditDto> {

    private final List<String> auditTableColumnNames;
    private final List<String> tableColumnNames;

    public TableAutidDtoRowMapper(TableMetadataProvider tableMetadataProvider,
                                  String tableName, String auditTable) {
        this.auditTableColumnNames = tableMetadataProvider.getAllColumnsForTable(auditTable);
        this.tableColumnNames = tableMetadataProvider.getAllColumnsForTable(tableName);
    }

    @Override
    public TableAuditDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return TableAuditDto.builder()
                .auditId(rs.getInt("audit_id"))
                .dateOp(rs.getString("date_op"))
                .codeOp(rs.getString("code_op"))
                .userOp(rs.getString("user_op"))
                .hostOp(rs.getString("host_op"))
                .tableId(rs.getInt(auditTableColumnNames.get(5)))
                .columnsBeforeChange(getColumnsBeforeChange(rs))
                .columnsAfterChange(getColumnsAfterChange(rs))
                .build();
    }

    private List<String> getColumnsAfterChange(ResultSet rs) {
        List<String> columnsAfterChange = auditTableColumnNames.subList(
                auditTableColumnNames.size() - tableColumnNames.size() + 1, auditTableColumnNames.size());
        return getDataFromResultSet(rs, columnsAfterChange);
    }

    private List<String> getColumnsBeforeChange(ResultSet rs) {
        List<String> columnsBeforeChange = auditTableColumnNames
                .subList(6, auditTableColumnNames.size() - tableColumnNames.size() + 1);
        return getDataFromResultSet(rs, columnsBeforeChange);
    }

    private List<String> getDataFromResultSet(ResultSet rs, List<String> subColumns) {
        return subColumns.stream()
                .map(col -> {
                    try {
                        return rs.getString(col);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();
    }
}
