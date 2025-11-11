package com.diploma.airline_data_logger.service;

import com.diploma.airline_data_logger.dto.TableAuditDto;
import com.diploma.airline_data_logger.dto.TableSchemaDto;

import java.util.List;

public interface DashboardService {

    List<TableSchemaDto> getTableSchemas();

    List<String> getAllAuditTableColumns(String tableName);

    List<TableAuditDto> loadDataFromAuditTable(String tableName, String startTime, String endTime);

    boolean doesAuditTableExist(String tableName);

    String restoreRecordInTable(String tableName, int id);

}
