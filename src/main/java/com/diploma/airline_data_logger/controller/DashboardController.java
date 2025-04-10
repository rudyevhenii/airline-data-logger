package com.diploma.airline_data_logger.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String getDashboard(Model model, Authentication authentication) {
        System.out.println(authentication);
        return "dashboard";
    }

}
