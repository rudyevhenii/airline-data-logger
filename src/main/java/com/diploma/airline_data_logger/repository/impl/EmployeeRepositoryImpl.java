package com.diploma.airline_data_logger.repository.impl;

import com.diploma.airline_data_logger.entity.Employee;
import com.diploma.airline_data_logger.repository.EmployeeRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Optional;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final JdbcTemplate jdbcTemplate;

    public EmployeeRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<Employee> findByEmail(String email) {
        String sql = "SELECT * FROM employees e WHERE e.email = ?";
        Employee employee = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Employee(
                rs.getInt("employee_id"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("role")), email);

        return Optional.of(employee);
    }

}
