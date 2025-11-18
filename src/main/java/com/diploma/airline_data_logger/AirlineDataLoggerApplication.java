package com.diploma.airline_data_logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AirlineDataLoggerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirlineDataLoggerApplication.class, args);
    }

}
