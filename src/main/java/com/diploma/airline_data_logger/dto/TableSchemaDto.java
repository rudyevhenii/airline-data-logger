package com.diploma.airline_data_logger.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TableSchemaDto {
    private String tableName;
    private List<String> columnNames;
    private boolean doesAuditTableExist;
    private boolean doesAuditTableLogging;
}
