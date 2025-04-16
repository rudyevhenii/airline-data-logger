package com.diploma.airline_data_logger.service;

import com.diploma.airline_data_logger.repository.TableAuditRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final TableAuditRepository tableAuditRepository;

    public DashboardService(TableAuditRepository tableAuditRepository) {
        this.tableAuditRepository = tableAuditRepository;
    }

    public Map<String, List<String>> getTableNames() {
        var tableColumns = tableAuditRepository.getAllTableColumns();

        return tableColumns;
    }

    public String createLogTableByTableName(String tableName) {
        String auditTable = tableName + "_audit";

        if (tableAuditRepository.doesTableExist(tableName)) {
            return "%s table already exists.".formatted(auditTable);
        }
        boolean created = tableAuditRepository.createLoggingTableByTableName(tableName);
        if (created) {
            return "%s table successfully created!".formatted(auditTable);
        }
        return "Something went wrong!";
    }
}
