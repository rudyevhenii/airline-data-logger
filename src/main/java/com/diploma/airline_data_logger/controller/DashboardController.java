package com.diploma.airline_data_logger.controller;

import com.diploma.airline_data_logger.service.DashboardService;
import com.diploma.airline_data_logger.service.TableService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;
    private final TableService tableService;

    public DashboardController(DashboardService dashboardService, TableService tableService) {
        this.dashboardService = dashboardService;
        this.tableService = tableService;
    }

    @GetMapping("/")
    public String redirectToDashboard() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
        var tableColumns = dashboardService.getTableNames();
        model.addAttribute("tableColumns", tableColumns);

        return "dashboard";
    }

    @GetMapping("/dashboard/create-log-table/{tableName}")
    public String createLoggingTable(@PathVariable String tableName, RedirectAttributes redirectAttributes) {
        String message = dashboardService.createLogTableByTableName(tableName);
        redirectAttributes.addFlashAttribute("successMessage", message);

        return "redirect:/dashboard";
    }

}
