package com.diploma.airline_data_logger.service;

import com.diploma.airline_data_logger.dto.TableAuditDto;
import com.diploma.airline_data_logger.dto.TableSchemaDto;
import com.diploma.airline_data_logger.repository.DashboardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    private final DashboardRepository dashboardRepository;

    public DashboardService(DashboardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

    public List<TableSchemaDto> getTableSchemas() {
        return dashboardRepository.getAllTableSchemas();
    }

    public List<String> getAllAuditTableColumns(String tableName) {
        return dashboardRepository.getAllAuditTableColumns(tableName);
    }

    public List<TableAuditDto> loadDataFromAuditTable(String tableName, String startTime, String endTime) {
        return dashboardRepository.loadDataFromAuditTable(tableName, startTime, endTime);
    }

    public boolean doesAuditTableExist(String tableName) {
        return dashboardRepository.doesAuditTableExist(tableName);
    }

    public String restoreRecordInTable(String tableName, int id) {
        boolean restored = dashboardRepository.restoreRecord(tableName, id);
        if (!restored) {
            throw new IllegalStateException("Something went wrong!");
        }
        return "The record in the table '%s' successfully restored!".formatted(tableName);
    }

}
