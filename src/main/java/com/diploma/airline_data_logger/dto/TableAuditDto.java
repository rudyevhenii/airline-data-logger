package com.diploma.airline_data_logger.dto;

import java.util.List;

public class TableAuditDto {

    private int id;
    private String dateOp;
    private String codeOp;
    private String userOp;
    private String hostOp;
    private List<String> columnsBeforeChange;
    private List<String> columnsAfterChange;

    public TableAuditDto() {
    }

    public TableAuditDto(int id, String dateOp, String codeOp, String userOp,
                         String hostOp, List<String> columnsBeforeChange, List<String> columnsAfterChange) {
        this.id = id;
        this.dateOp = dateOp;
        this.codeOp = codeOp;
        this.userOp = userOp;
        this.hostOp = hostOp;
        this.columnsBeforeChange = columnsBeforeChange;
        this.columnsAfterChange = columnsAfterChange;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateOp() {
        return dateOp;
    }

    public void setDateOp(String dateOp) {
        this.dateOp = dateOp;
    }

    public String getCodeOp() {
        return codeOp;
    }

    public void setCodeOp(String codeOp) {
        this.codeOp = codeOp;
    }

    public String getUserOp() {
        return userOp;
    }

    public void setUserOp(String userOp) {
        this.userOp = userOp;
    }

    public String getHostOp() {
        return hostOp;
    }

    public void setHostOp(String hostOp) {
        this.hostOp = hostOp;
    }

    public List<String> getColumnsBeforeChange() {
        return columnsBeforeChange;
    }

    public void setColumnsBeforeChange(List<String> columnsBeforeChange) {
        this.columnsBeforeChange = columnsBeforeChange;
    }

    public List<String> getColumnsAfterChange() {
        return columnsAfterChange;
    }

    public void setColumnsAfterChange(List<String> columnsAfterChange) {
        this.columnsAfterChange = columnsAfterChange;
    }

}
