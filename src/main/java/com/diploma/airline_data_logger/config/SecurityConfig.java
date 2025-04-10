package com.diploma.airline_data_logger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                .requestMatchers("/dashboard").authenticated()
                .requestMatchers("/login").permitAll());
//        http.formLogin(Customizer.withDefaults());
        http.formLogin(flc -> flc.loginPage("/login")
                .defaultSuccessUrl("/dashboard")
                .failureUrl("/login?error=true")
                .usernameParameter("email")
                .passwordParameter("password"));
        http.httpBasic(Customizer.withDefaults());

        return http.build();
    }
//    @Bean
//    public PasswordEncoder passwordEncoderFactories() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }
}
