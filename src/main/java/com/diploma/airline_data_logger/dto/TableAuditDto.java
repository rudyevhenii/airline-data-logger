package com.diploma.airline_data_logger.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableAuditDto {
    private int auditId;
    private String dateOp;
    private String codeOp;
    private String userOp;
    private String hostOp;
    private int tableId;
    private List<String> columnsBeforeChange;
    private List<String> columnsAfterChange;
}
