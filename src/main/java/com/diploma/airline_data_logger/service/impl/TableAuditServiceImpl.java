package com.diploma.airline_data_logger.service.impl;

import com.diploma.airline_data_logger.repository.TableAuditRepository;
import com.diploma.airline_data_logger.repository.TableMetadataProvider;
import com.diploma.airline_data_logger.service.TableAuditService;
import org.springframework.stereotype.Service;

@Service
public class TableAuditServiceImpl implements TableAuditService {

    private final TableAuditRepository tableAuditRepository;
    private final TableMetadataProvider tableMetadataProvider;

    public TableAuditServiceImpl(TableAuditRepository tableAuditRepository,
                                 TableMetadataProvider tableMetadataProvider) {
        this.tableAuditRepository = tableAuditRepository;
        this.tableMetadataProvider = tableMetadataProvider;
    }

    @Override
    public String createAuditTableByTableName(String tableName) {
        String auditTable = "audit_" + tableName;

        if (tableMetadataProvider.doesTableExist(auditTable)) {
            throw new IllegalStateException("'%s' table already exists.".formatted(auditTable));
        }
        tableAuditRepository.createAuditTable(tableName);

        return "'%s' table successfully created!".formatted(auditTable);
    }

    @Override
    public String createTriggersForTable(String tableName) {
        throwExceptionIfAuditTableDoesNotExist(tableName);

        if (tableMetadataProvider.doTriggersExistForTable(tableName)) {
            throw new IllegalStateException("Triggers for table '%s' already exist!".formatted(tableName));
        }
        tableAuditRepository.createTriggersForTable(tableName);

        return "Triggers for table '%s' successfully created!".formatted(tableName);
    }

    private void throwExceptionIfAuditTableDoesNotExist(String tableName) {
        String auditTable = "audit_" + tableName;
        if (!tableMetadataProvider.doesTableExist(auditTable)) {
            throw new IllegalStateException("Audit table should be created first!");
        }
    }

    @Override
    public String deleteTriggersByTableName(String tableName) {
        throwExceptionIfAuditTableDoesNotExist(tableName);

        if (!tableMetadataProvider.doTriggersExistForTable(tableName)) {
            throw new IllegalStateException("Triggers for table '%s' do not exist!".formatted(tableName));
        }
        tableAuditRepository.deleteTriggersForTable(tableName);

        return "Triggers for table '%s' successfully deleted!".formatted(tableName);
    }

    @Override
    public String deleteAuditTableByTableName(String tableName) {
        String auditTable = "audit_" + tableName;
        throwExceptionIfAuditTableDoesNotExist(tableName);

        if (tableMetadataProvider.doTriggersExistForTable(tableName)) {
            tableAuditRepository.deleteTriggersForTable(tableName);
        }
        tableAuditRepository.deleteAuditTable(tableName);

        return "'%s' table successfully deleted!".formatted(auditTable);
    }

}
