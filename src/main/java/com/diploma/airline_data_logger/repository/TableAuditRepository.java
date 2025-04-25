package com.diploma.airline_data_logger.repository;

import java.util.List;

public interface TableAuditRepository {

    List<String> getAllTableNames();

    List<String> getAllColumnsForTable(String tableName);

    List<String> getAllColumnsDataType(String tableName);

    void createAuditTable(String tableName);

    boolean doesAuditTableExist(String tableName);

    void deleteAuditTable(String tableName);

    void createTriggersForTable(String tableName);

    boolean doTriggersExistForTable(String tableName);

    void deleteTriggersForTable(String tableName);

}
