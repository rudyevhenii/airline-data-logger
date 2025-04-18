package com.diploma.airline_data_logger.dto;

import java.util.List;

public class TableSchemaDto {

    private String tableName;
    private List<String> columnNames;
    private boolean doesAuditTableExist;
    private boolean doesAuditTableLogging;

    public TableSchemaDto() {
    }

    public TableSchemaDto(String tableName, List<String> columnNames,
                          boolean doesAuditTableExist, boolean doesAuditTableLogging) {
        this.tableName = tableName;
        this.columnNames = columnNames;
        this.doesAuditTableExist = doesAuditTableExist;
        this.doesAuditTableLogging = doesAuditTableLogging;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    public boolean isDoesAuditTableExist() {
        return doesAuditTableExist;
    }

    public void setDoesAuditTableExist(boolean doesAuditTableExist) {
        this.doesAuditTableExist = doesAuditTableExist;
    }

    public boolean isDoesAuditTableLogging() {
        return doesAuditTableLogging;
    }

    public void setDoesAuditTableLogging(boolean doesAuditTableLogging) {
        this.doesAuditTableLogging = doesAuditTableLogging;
    }

}
