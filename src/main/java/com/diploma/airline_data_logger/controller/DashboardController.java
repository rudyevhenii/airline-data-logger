package com.diploma.airline_data_logger.controller;

import com.diploma.airline_data_logger.dto.TableAuditDto;
import com.diploma.airline_data_logger.dto.TableSchemaDto;
import com.diploma.airline_data_logger.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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
        List<TableSchemaDto> tableSchemas = dashboardService.getTableSchemas();
        model.addAttribute("tableSchemas", tableSchemas);

        return "dashboard";
    }

    @GetMapping("/dashboard/table-audit/{tableName}")
    public String loadDataFromAuditTable(@PathVariable String tableName,
                                         @RequestParam(required = false, defaultValue = "") String startTime,
                                         @RequestParam(required = false, defaultValue = "") String endTime,
                                         Model model, RedirectAttributes redirectAttributes) {
        if (!dashboardService.doesAuditTableExist(tableName)) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "'audit_%s' table does not exist!".formatted(tableName));

            return "redirect:/dashboard";
        }
        List<TableAuditDto> tableAuditList = dashboardService.loadDataFromAuditTable(tableName, startTime, endTime);
        List<String> tableAuditColumns = dashboardService.getAllTableAuditColumns(tableName);

        model.addAttribute("recordCount", tableAuditList.size());
        model.addAttribute("tableName", tableName);
        model.addAttribute("tableAuditList", tableAuditList);
        model.addAttribute("tableAuditColumns", tableAuditColumns);

        return "audit-table";
    }

    @GetMapping("/dashboard/restore/{tableName}")
    public String restoreRecordInTable(@PathVariable String tableName, @RequestParam int id,
                                       RedirectAttributes redirectAttributes) {
        String message = dashboardService.restoreRecordInTable(tableName, id);
        redirectAttributes.addFlashAttribute("successMessage", message);

        return "redirect:/dashboard/table-audit/" + tableName;
    }

}
