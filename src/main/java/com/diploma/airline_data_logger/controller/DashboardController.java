package com.diploma.airline_data_logger.controller;

import com.diploma.airline_data_logger.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/")
    public String redirectToDashboard() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
        var tableSchemas = dashboardService.getTableSchemas();
        model.addAttribute("tableSchemas", tableSchemas);

        return "dashboard";
    }

}
