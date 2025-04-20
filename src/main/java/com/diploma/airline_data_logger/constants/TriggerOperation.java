package com.diploma.airline_data_logger.constants;

public enum TriggerOperation {

    INSERT("Insert"),
    UPDATE("Update"),
    DELETE("Delete");

    private final String operation;

    TriggerOperation(String operation) {
        this.operation = operation;
    }

    public String getOperationNameLowerCase() {
        return operation.toLowerCase();
    }

    public String getOperationNameUpperCase() {
        return operation.toUpperCase();
    }

}
