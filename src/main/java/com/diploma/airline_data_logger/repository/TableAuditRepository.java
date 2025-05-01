package com.diploma.airline_data_logger.repository;

public interface TableAuditRepository {

    void createAuditTable(String tableName);

    void deleteAuditTable(String tableName);

    void createTriggersForTable(String tableName);

    void deleteTriggersForTable(String tableName);

}
