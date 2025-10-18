package com.diploma.airline_data_logger.repository;

import com.diploma.airline_data_logger.entity.Employee;

import java.util.Optional;

public interface EmployeeRepository {

    Optional<Employee> findByEmail(String email);

}
