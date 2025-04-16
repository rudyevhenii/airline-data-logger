package com.diploma.airline_data_logger.service;

import com.diploma.airline_data_logger.repository.TableAuditRepository;
import org.springframework.stereotype.Service;

@Service
public class TableService {

    private final TableAuditRepository tableAuditRepository;

    public TableService(TableAuditRepository tableAuditRepository) {
        this.tableAuditRepository = tableAuditRepository;
    }

}
