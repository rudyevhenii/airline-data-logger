package com.diploma.airline_data_logger.service.impl;

import com.diploma.airline_data_logger.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final String AUDIT_TABLE_PREFIX = "audit_";

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Value("classpath:stringTemplates/emailMessageBody.st")
    private Resource emailMessageBodyTemplate;

    @Override
    @Async
    public void sendSimpleMessage(String tableName) {
        try {
            String adminEmail = getCurrentAdminEmail();
            String subject = "Audit Table Creation Confirmation [Airline Data Logger]";
            String textTemplate = emailMessageBodyTemplate.getContentAsString(StandardCharsets.UTF_8);

            String resolvedText = resolveTextParams(textTemplate,
                    tableName, AUDIT_TABLE_PREFIX + tableName, LocalDateTime.now());

            sendMessage(adminEmail, subject, resolvedText);
        } catch (Exception e) {
            log.error("Failed to send email notification for audit table creation: {}", e.getMessage());
        }
    }

    private String resolveTextParams(String textTemplate, Object... args) {
        String formattedText = textTemplate;
        for (Object arg : args) {
            formattedText = formattedText.replaceFirst("\\{}", arg.toString());
        }
        return formattedText;
    }

    private void sendMessage(String adminEmail, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailFrom);
            message.setTo(adminEmail);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            log.info("Email notification sent successfully to {}", adminEmail);
        } catch (Exception e) {
            log.error("Error while sending email to {}: {}", adminEmail, e.getMessage());
        }
    }

    private String getCurrentAdminEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            return (String) principal;
        }

        log.warn("Could not determine Principal type: {}", principal.getClass().getName());
        return null;
    }

}
