package com.diploma.airline_data_logger.repository.impl;

import com.diploma.airline_data_logger.repository.TableMetadataProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.diploma.airline_data_logger.query.SqlQuery.*;

@Repository
@RequiredArgsConstructor
public class TableMetadataProviderImpl implements TableMetadataProvider {

    private final JdbcTemplate jdbcTemplate;

    @Value("${application.database.name}")
    private String databaseName;

    @Override
    public List<String> getAllTableNames() {
        return jdbcTemplate.queryForList(SELECT_ALL_TABLE_NAMES_SQL.formatted(databaseName),
                String.class);
    }

    @Override
    public List<String> getAllColumnsForTable(String tableName) {
        return jdbcTemplate.queryForList(SELECT_ALL_TABLE_COLUMNS_SQL.formatted(databaseName),
                String.class, tableName);
    }

    @Override
    public List<String> getAllColumnsDataType(String tableName) {
        return jdbcTemplate.queryForList(SELECT_ALL_DATA_TYPE_COLUMNS_SQL.formatted(databaseName),
                String.class, tableName);
    }

    @Override
    public boolean doesTableExist(String tableName) {
        Optional<Integer> countOptional = Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_COUNT_TABLES_SQL
                        .formatted(databaseName), Integer.class, tableName));
        return countOptional.map(count -> count > 0)
                .orElse(false);
    }

    @Override
    public boolean doTriggersExistForTable(String tableName) {
        Optional<Integer> countOptional = Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_COUNT_TABLE_TRIGGERS_SQL
                .formatted(databaseName), Integer.class, tableName));
        return countOptional.map(count -> count > 0)
                .orElse(false);
    }

}
