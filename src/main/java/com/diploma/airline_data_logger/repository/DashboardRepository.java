package com.diploma.airline_data_logger.repository;

import com.diploma.airline_data_logger.dto.TableAuditDto;
import com.diploma.airline_data_logger.dto.TableSchemaDto;

import java.util.List;

public interface DashboardRepository {

    List<TableSchemaDto> getAllTableSchemas();

    List<String> getAllTableAuditColumns(String tableName);

    List<TableAuditDto> loadDataFromAuditTable(String tableName, String startTime, String endTime);

    boolean doesAuditTableExist(String tableName);

    void restoreRecord(String tableName, int id);

    TableAuditDto findRecordInAuditTableById(String tableName, int id);

}
