package com.diploma.airline_data_logger.entity;

public class Employee {

    private int employeeId;

    private String email;

    private String password;

    private String role;

    public Employee() {
    }

    public Employee(int employeeId, String email, String password, String role) {
        this.employeeId = employeeId;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
