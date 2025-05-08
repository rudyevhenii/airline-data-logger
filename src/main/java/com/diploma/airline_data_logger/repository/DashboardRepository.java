package com.diploma.airline_data_logger.repository;

import com.diploma.airline_data_logger.dto.TableAuditDto;
import com.diploma.airline_data_logger.dto.TableSchemaDto;

import java.util.List;
import java.util.Optional;

public interface DashboardRepository {

    List<TableSchemaDto> getAllTableSchemas();

    List<String> getAllAuditTableColumns(String tableName);

    List<TableAuditDto> loadDataFromAuditTable(String tableName, String startTime, String endTime);

    boolean doesAuditTableExist(String tableName);

    boolean restoreRecord(String tableName, int id);

    Optional<TableAuditDto> findRecordInAuditTableById(String tableName, int id);

}
