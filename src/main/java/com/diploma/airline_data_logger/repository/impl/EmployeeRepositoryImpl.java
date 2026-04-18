package com.diploma.airline_data_logger.repository.impl;

import com.diploma.airline_data_logger.entity.Employee;
import com.diploma.airline_data_logger.query.SqlQuery;
import com.diploma.airline_data_logger.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Employee> findByEmail(String email) {
        Employee employee = jdbcTemplate.queryForObject(SqlQuery.SELECT_ALL_FROM_EMPLOYEES,
                (rs, rowNum) -> new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("name")), email);

        return Optional.ofNullable(employee);
    }

}
