package com.diploma.airline_data_logger.repository;

import java.util.List;

public interface TableAuditRepository {

    List<String> getAllTableNames();

    List<String> getAllColumnsForTable(String tableName);

    List<String> getAllColumnsDataType(String tableName);

    void createAuditTableByTableName(String tableName);

    boolean doesAuditTableExist(String tableName);

    void deleteAuditTableByTableName(String tableName);

    void createTriggersForTable(String tableName);

    boolean doTriggersExistForTable(String tableName);

    void deleteTriggersByTableName(String tableName);

}
