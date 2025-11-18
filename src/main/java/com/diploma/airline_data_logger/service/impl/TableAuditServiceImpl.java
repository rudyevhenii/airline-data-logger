package com.diploma.airline_data_logger.service.impl;

import com.diploma.airline_data_logger.repository.TableAuditRepository;
import com.diploma.airline_data_logger.repository.TableMetadataProvider;
import com.diploma.airline_data_logger.service.EmailService;
import com.diploma.airline_data_logger.service.TableAuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TableAuditServiceImpl implements TableAuditService {

    private static final Logger logger = LoggerFactory.getLogger(TableAuditServiceImpl.class);

    private final TableAuditRepository tableAuditRepository;
    private final TableMetadataProvider tableMetadataProvider;
    private final EmailService emailService;

    public TableAuditServiceImpl(TableAuditRepository tableAuditRepository,
                             TableMetadataProvider tableMetadataProvider,
                             EmailService emailService) {
        this.tableAuditRepository = tableAuditRepository;
        this.tableMetadataProvider = tableMetadataProvider;
        this.emailService = emailService;
    }

    @Override
    public String createAuditTableByTableName(String tableName) {
        String auditTable = "audit_" + tableName;

        if (tableMetadataProvider.doesTableExist(auditTable)) {
            throw new IllegalStateException("'%s' table already exists.".formatted(auditTable));
        }
        tableAuditRepository.createAuditTable(tableName);

        try {
            String adminEmail = getCurrentAdminEmail();
            if (adminEmail != null) {
                String subject = "Audit Table Creation Confirmation [Airline Data Logger]";
                String text = String.format(
                        "You have successfully created a new audit table.\n\n" +
                                "Operation Details:\n" +
                                "  Original Table: %s\n" +
                                "  New Audit Table: %s\n" +
                                "  Operation Time: %s\n",
                        tableName, auditTable, LocalDateTime.now()
                );

                emailService.sendSimpleMessage(adminEmail, subject, text);
            } else {
                logger.warn("Could not retrieve current admin email. Audit creation notification not sent.");
            }
        } catch (Exception e) {
            logger.error("Failed to send email notification for audit table creation: {}", e.getMessage());
        }

        return "'%s' table successfully created!".formatted(auditTable);
    }

    private String getCurrentAdminEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            return (String) principal;
        }

        logger.warn("Could not determine Principal type: {}", principal.getClass().getName());
        return null;
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
