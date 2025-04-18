package com.diploma.airline_data_logger.repository;

import com.diploma.airline_data_logger.dto.TableSchemaDto;

import java.util.List;

public interface DashboardRepository {

    List<TableSchemaDto> getAllTableSchemas();

}
