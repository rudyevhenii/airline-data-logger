package com.diploma.airline_data_logger.repository.impl;

import com.diploma.airline_data_logger.dto.TableAuditDto;
import com.diploma.airline_data_logger.dto.TableSchemaDto;
import com.diploma.airline_data_logger.repository.DashboardRepository;
import com.diploma.airline_data_logger.repository.TableAuditRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DashboardRepositoryImpl implements DashboardRepository {

    private final TableAuditRepository tableAuditRepository;
    private final JdbcTemplate jdbcTemplate;

    public DashboardRepositoryImpl(TableAuditRepository tableAuditRepository, DataSource dataSource) {
        this.tableAuditRepository = tableAuditRepository;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<TableSchemaDto> getAllTableSchemas() {
        List<TableSchemaDto> tableSchemas = new ArrayList<>();
        List<String> allTableNames = tableAuditRepository.getAllTableNames();

        for (String allTableName : allTableNames) {
            TableSchemaDto tableSchemaDto = new TableSchemaDto(
                    allTableName,
                    tableAuditRepository.getAllColumnsForTable(allTableName),
                    tableAuditRepository.doesAuditTableExist(allTableName),
                    tableAuditRepository.doTriggersExistForTable(allTableName));

            tableSchemas.add(tableSchemaDto);
        }
        return tableSchemas;
    }

    @Override
    public List<String> getAllTableAuditColumns(String tableName) {
        String auditTable = "audit_" + tableName;
        return tableAuditRepository.getAllColumnsForTable(auditTable);
    }

    @Override
    public List<TableAuditDto> loadDataFromAuditTable(String tableName,
                                                      String startTime, String endTime) {
        String auditTable = "audit_" + tableName;
        List<String> auditTableNames = tableAuditRepository.getAllColumnsForTable(auditTable);
        List<String> tableNames = tableAuditRepository.getAllColumnsForTable(tableName);

        String sql = getDateFiltrationSql(auditTable, startTime, endTime);

        return jdbcTemplate.query(sql, new RowMapper<TableAuditDto>() {

            @Override
            public TableAuditDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                TableAuditDto tableAuditDto = new TableAuditDto();
                tableAuditDto.setId(rs.getInt("id"));
                tableAuditDto.setDateOp(rs.getString("date_op"));
                tableAuditDto.setCodeOp(rs.getString("code_op"));
                tableAuditDto.setUserOp(rs.getString("user_op"));
                tableAuditDto.setHostOp(rs.getString("host_op"));

                List<String> columnsBeforeChange = auditTableNames
                        .subList(5, auditTableNames.size() - tableNames.size() + 1);
                List<String> columnsAfterChange = auditTableNames.subList(
                        auditTableNames.size() - tableNames.size() + 1, auditTableNames.size());

                List<String> dataFromColumnsBeforeChange = getDataFromResultSet(rs, columnsBeforeChange);
                List<String> dataFromColumnsAfterChange = getDataFromResultSet(rs, columnsAfterChange);

                tableAuditDto.setColumnsBeforeChange(dataFromColumnsBeforeChange);
                tableAuditDto.setColumnsAfterChange(dataFromColumnsAfterChange);

                return tableAuditDto;
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
        });
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

    @Override
    public boolean doesAuditTableExist(String tableName) {
        return tableAuditRepository.doesAuditTableExist(tableName);
    }

}
