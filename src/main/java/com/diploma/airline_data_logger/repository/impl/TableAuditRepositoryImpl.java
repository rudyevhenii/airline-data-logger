package com.diploma.airline_data_logger.repository.impl;

import com.diploma.airline_data_logger.repository.TableAuditRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Repository
public class TableAuditRepositoryImpl implements TableAuditRepository {

    private final JdbcTemplate jdbcTemplate;

    public TableAuditRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Map<String, List<String>> getAllTableColumns() {
        String sql = """
                SELECT table_name
                FROM information_schema.TABLES
                WHERE table_schema = 'airline_data_logger'
                AND table_name <> 'employees'
                AND NOT table_name LIKE '%_audit';""";

        Map<String, List<String>> tableColumns = new HashMap<>();
        List<String> tableNames = jdbcTemplate.queryForList(sql, String.class);

        for (String tableName : tableNames) {
            tableColumns.put(tableName, getColumnsForTable(tableName));
        }
        return tableColumns;
    }

    @Override
    public List<String> getColumnsForTable(String tableName) {
        String sql = """
                SELECT column_name
                FROM information_schema.COLUMNS
                WHERE table_name = ?
                AND table_schema = 'airline_data_logger';""";

        return jdbcTemplate.queryForList(sql, String.class, tableName);
    }

    @Override
    public boolean createLoggingTableByTableName(String tableName) {
        String columnsBeforeChange = getColumnsStructure(tableName, "_");
        String columnsAfterChange = getColumnsStructure(tableName, "");
        String completeColumnsStructure = columnsBeforeChange + columnsAfterChange;

        String auditTable = tableName + "_audit";
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

        return doesTableExist(tableName);
    }

    private String getColumnsStructure(String tableName, String suffix) {
        List<String> columnNames = getColumnsForTable(tableName);
        List<String> columnDataTypes = getColumnsDataType(tableName);

        boolean applyDelimiter = suffix.equals("_");
        int columnLength = columnNames.size();

        return IntStream.range(0, columnLength)
                .mapToObj(i -> "\t%s %s%s%s".formatted(columnNames.get(i) + suffix,
                        columnDataTypes.get(i),
                        columnDataTypes.get(i).equalsIgnoreCase("varchar") ? "(100)" : "",
                        applyDelimiter ? ",\n" : ""))
                .collect(Collectors.joining(!applyDelimiter ? ",\n" : ""));
    }

    @Override
    public List<String> getColumnsDataType(String tableName) {
        String sql = """
                SELECT data_type
                FROM information_schema.COLUMNS
                WHERE table_name = ?
                AND table_schema = 'airline_data_logger';""";

        return jdbcTemplate.queryForList(sql, String.class, tableName);
    }

    @Override
    public boolean doesTableExist(String tableName) {
        String auditTable = tableName + "_audit";
        String sql = """
                SELECT COUNT(*)
                FROM information_schema.TABLES
                WHERE table_name = ?
                AND table_schema = 'airline_data_logger';""";

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, auditTable);
        return count > 0;
    }

}
