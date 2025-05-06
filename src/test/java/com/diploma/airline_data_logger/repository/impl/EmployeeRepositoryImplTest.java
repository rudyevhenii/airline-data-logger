package com.diploma.airline_data_logger.repository.impl;

import com.diploma.airline_data_logger.entity.Employee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class EmployeeRepositoryImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private EmployeeRepositoryImpl underTest;

    @Test
    void givenEmployeeEmail_whenFindByEmail_thenReturnEmployee() {
        // given
        Employee employee = new Employee(1, "user@gmail.com", "12345", "USER");
        String email = employee.getEmail();

        given(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq(email)))
                .willReturn(employee);

        // when
        Optional<Employee> result = underTest.findByEmail(email);

        // then
        assertThat(result).isEqualTo(Optional.of(employee));
    }

}