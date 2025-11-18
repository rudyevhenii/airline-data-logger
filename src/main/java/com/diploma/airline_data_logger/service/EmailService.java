package com.diploma.airline_data_logger.service;

public interface EmailService {

    void sendSimpleMessage(String to, String subject, String text);

}