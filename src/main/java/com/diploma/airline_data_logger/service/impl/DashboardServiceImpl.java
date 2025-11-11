package com.diploma.airline_data_logger.service.impl;

import com.diploma.airline_data_logger.dto.TableAuditDto;
import com.diploma.airline_data_logger.dto.TableSchemaDto;
import com.diploma.airline_data_logger.repository.DashboardRepository;
import com.diploma.airline_data_logger.service.DashboardService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final DashboardRepository dashboardRepository;

    public DashboardServiceImpl(DashboardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

    @Override
    public List<TableSchemaDto> getTableSchemas() {
        return dashboardRepository.getAllTableSchemas();
    }

    @Override
    public List<String> getAllAuditTableColumns(String tableName) {
        return dashboardRepository.getAllAuditTableColumns(tableName);
    }

    @Override
    public List<TableAuditDto> loadDataFromAuditTable(String tableName, String startTime, String endTime) {
        return dashboardRepository.loadDataFromAuditTable(tableName, startTime, endTime);
    }

    @Override
    public boolean doesAuditTableExist(String tableName) {
        return dashboardRepository.doesAuditTableExist(tableName);
    }

    @Override
    public String restoreRecordInTable(String tableName, int id) {
        boolean restored = dashboardRepository.restoreRecord(tableName, id);
        if (!restored) {
            throw new IllegalStateException("Something went wrong!");
        }
        return "The record in the table '%s' successfully restored!".formatted(tableName);
    }

}
