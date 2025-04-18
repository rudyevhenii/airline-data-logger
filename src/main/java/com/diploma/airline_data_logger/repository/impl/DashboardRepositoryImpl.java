package com.diploma.airline_data_logger.repository.impl;

import com.diploma.airline_data_logger.dto.TableSchemaDto;
import com.diploma.airline_data_logger.repository.DashboardRepository;
import com.diploma.airline_data_logger.repository.TableAuditRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DashboardRepositoryImpl implements DashboardRepository {

    private final TableAuditRepository tableAuditRepository;

    public DashboardRepositoryImpl(TableAuditRepository tableAuditRepository) {
        this.tableAuditRepository = tableAuditRepository;
    }

    @Override
    public List<TableSchemaDto> getAllTableSchemas() {
        List<TableSchemaDto> tableSchemas = new ArrayList<>();
        List<String> allTableNames = tableAuditRepository.getAllTableNames();

        for (String allTableName : allTableNames) {
            TableSchemaDto tableSchemaDto = new TableSchemaDto(
                    allTableName,
                    tableAuditRepository.getAllColumnsForTable(allTableName),
                    tableAuditRepository.doesAuditTableExist(allTableName),
                    tableAuditRepository.doTriggersExistForTable(allTableName));

            tableSchemas.add(tableSchemaDto);
        }
        return tableSchemas;
    }

}
