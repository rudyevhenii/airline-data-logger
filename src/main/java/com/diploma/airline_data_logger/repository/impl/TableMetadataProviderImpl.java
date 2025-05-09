package com.diploma.airline_data_logger.repository.impl;

import com.diploma.airline_data_logger.repository.TableMetadataProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TableMetadataProviderImpl implements TableMetadataProvider {

    private final JdbcTemplate jdbcTemplate;

    @Value("${application.database.name}")
    private String databaseName;

    public TableMetadataProviderImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<String> getAllTableNames() {
        String sql = """
                SELECT table_name
                FROM information_schema.TABLES
                WHERE table_schema = '%s'
                AND table_name <> 'employees'
                AND table_name <> 'roles'
                AND NOT table_name LIKE 'audit_%%';
                """.formatted(databaseName);

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
                """.formatted(databaseName);

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
                """.formatted(databaseName);

        return jdbcTemplate.queryForList(sql, String.class, tableName);
    }

    @Override
    public boolean doesTableExist(String tableName) {
        String sql = """
                SELECT COUNT(*)
                FROM information_schema.TABLES
                WHERE table_name = ?
                AND table_schema = '%s';
                """.formatted(databaseName);

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName);
        return count > 0;
    }

    @Override
    public boolean doTriggersExistForTable(String tableName) {
        String sql = """
                SELECT COUNT(*)
                FROM information_schema.TRIGGERS
                WHERE EVENT_OBJECT_TABLE = ?
                AND TRIGGER_SCHEMA = '%s';
                """.formatted(databaseName);

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName);
        return count > 0;
    }

}
