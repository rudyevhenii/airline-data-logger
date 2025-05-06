package com.diploma.airline_data_logger.repository.impl;

import com.diploma.airline_data_logger.entity.Employee;
import com.diploma.airline_data_logger.repository.EmployeeRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final JdbcTemplate jdbcTemplate;

    public EmployeeRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Employee> findByEmail(String email) {
        String sql = """
                SELECT e.employee_id, e.email, e.password, r.name
                FROM employees e
                JOIN roles r ON e.role_id = r.role_id
                WHERE e.email = ?;""";

        Employee employee = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Employee(
                rs.getInt("employee_id"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("name")), email);

        return Optional.of(employee);
    }

}
