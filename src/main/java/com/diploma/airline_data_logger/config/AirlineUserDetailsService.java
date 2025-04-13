package com.diploma.airline_data_logger.config;

import com.diploma.airline_data_logger.entity.Employee;
import com.diploma.airline_data_logger.entity.Role;
import com.diploma.airline_data_logger.repository.EmployeeRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AirlineUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    public AirlineUserDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Employee employee = employeeRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("User with email " + username + " is not found"));
        Role role = employee.getRole();

        return User.builder()
                .username(employee.getEmail())
                .password(employee.getPassword())
                .roles(role.getName())
                .build();
    }
}
