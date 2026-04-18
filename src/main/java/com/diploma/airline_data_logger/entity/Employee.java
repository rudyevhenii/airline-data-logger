package com.diploma.airline_data_logger.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private int employeeId;
    private String email;
    private String password;
    private String role;
}
