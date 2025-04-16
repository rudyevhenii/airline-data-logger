package com.diploma.airline_data_logger.repository;

import java.util.List;
import java.util.Map;

public interface TableAuditRepository {

    Map<String, List<String>> getAllTableColumns();

    List<String> getColumnsForTable(String tableName);

    boolean createLoggingTableByTableName(String tableName);

    List<String> getColumnsDataType(String tableName);

    boolean doesTableExist(String tableName);
}
