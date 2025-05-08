package com.diploma.airline_data_logger.repository;

import java.util.List;

public interface TableMetadataProvider {

    List<String> getAllTableNames();

    List<String> getAllColumnsForTable(String tableName);

    List<String> getAllColumnsDataType(String tableName);

    boolean doesTableExist(String tableName);

    boolean doTriggersExistForTable(String tableName);

}
