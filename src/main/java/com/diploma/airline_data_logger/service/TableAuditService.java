package com.diploma.airline_data_logger.service;

import com.diploma.airline_data_logger.repository.TableAuditRepository;
import com.diploma.airline_data_logger.repository.TableMetadataProvider;
import org.springframework.stereotype.Service;

@Service
public class TableAuditService {

    private final TableAuditRepository tableAuditRepository;
    private final TableMetadataProvider tableMetadataProvider;

    public TableAuditService(TableAuditRepository tableAuditRepository,
                             TableMetadataProvider tableMetadataProvider) {
        this.tableAuditRepository = tableAuditRepository;
        this.tableMetadataProvider = tableMetadataProvider;
    }

    public String createAuditTableByTableName(String tableName) {
        String auditTable = "audit_" + tableName;

        if (tableMetadataProvider.doesAuditTableExist(tableName)) {
            throw new IllegalStateException("'%s' table already exists.".formatted(auditTable));
        }
        tableAuditRepository.createAuditTable(tableName);

        return "'%s' table successfully created!".formatted(auditTable);
    }

    public String createTriggersForTable(String tableName) {
        if (!tableMetadataProvider.doesAuditTableExist(tableName)) {
            throw new IllegalStateException("Audit table should be created first!");
        }

        if (tableMetadataProvider.doesAuditTableExist(tableName)) {
            if (tableMetadataProvider.doTriggersExistForTable(tableName)) {
                throw new IllegalStateException("Triggers for table %s already exist!".formatted(tableName));
            }
            tableAuditRepository.createTriggersForTable(tableName);
        }
        return "Triggers for table '%s' successfully created!".formatted(tableName);
    }

    public String deleteTriggersByTableName(String tableName) {
        if (!tableMetadataProvider.doesAuditTableExist(tableName)) {
            throw new IllegalStateException("Audit table should be created first!");
        }

        if (tableMetadataProvider.doesAuditTableExist(tableName)) {
            if (!tableMetadataProvider.doTriggersExistForTable(tableName)) {
                throw new IllegalStateException("Triggers for table '%s' do not exist!".formatted(tableName));
            }
            tableAuditRepository.deleteTriggersForTable(tableName);
        }
        return "Triggers for table '%s' successfully deleted!".formatted(tableName);
    }

    public String deleteAuditTableByTableName(String tableName) {
        String auditTable = "audit_" + tableName;
        if (!tableMetadataProvider.doesAuditTableExist(tableName)) {
            throw new IllegalStateException("Table '%s' does not exist!".formatted(auditTable));
        }

        if (tableMetadataProvider.doesAuditTableExist(tableName)) {
            if (tableMetadataProvider.doTriggersExistForTable(tableName)) {
                tableAuditRepository.deleteTriggersForTable(tableName);
            }
            tableAuditRepository.deleteAuditTable(tableName);
        }
        return "'%s' table successfully deleted!".formatted(auditTable);
    }

}
