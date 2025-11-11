package com.diploma.airline_data_logger.service;

public interface TableAuditService {

    String createAuditTableByTableName(String tableName);

    String createTriggersForTable(String tableName);

    String deleteTriggersByTableName(String tableName);

    String deleteAuditTableByTableName(String tableName);

}
