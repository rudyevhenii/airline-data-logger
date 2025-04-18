package com.diploma.airline_data_logger.controller;

import com.diploma.airline_data_logger.service.TableAuditService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tables")
public class TableAuditController {

    private final TableAuditService tableAuditService;

    public TableAuditController(TableAuditService tableAuditService) {
        this.tableAuditService = tableAuditService;
    }

    @GetMapping("/{tableName}")
    public String getAuditTableLogs(@PathVariable String tableName,
                                    Model model) {
        model.addAttribute("tableName", tableName);

        return "table";
    }

    @GetMapping("/create-log-table/{tableName}")
    public String createAuditTable(@PathVariable String tableName,
                                   RedirectAttributes redirectAttributes) {
        String message = tableAuditService.createAuditTableByTableName(tableName);
        redirectAttributes.addFlashAttribute("successMessage", message);

        return "redirect:/dashboard";
    }

    @GetMapping("/delete-log-table/{tableName}")
    public String deleteAuditTable(@PathVariable String tableName,
                                   RedirectAttributes redirectAttributes) {
        String message = tableAuditService.deleteAuditTableByTableName(tableName);
        redirectAttributes.addFlashAttribute("successMessage", message);

        return "redirect:/dashboard";
    }

    @GetMapping("/create-triggers/{tableName}")
    public String createTriggersForTable(@PathVariable String tableName,
                                         RedirectAttributes redirectAttributes) {
        String message = tableAuditService.createTriggersForTable(tableName);
        redirectAttributes.addFlashAttribute("successMessage", message);

        return "redirect:/dashboard";
    }

    @GetMapping("/delete-triggers/{tableName}")
    public String deleteTriggersByTableName(@PathVariable String tableName,
                                            RedirectAttributes redirectAttributes) {
        String message = tableAuditService.deleteTriggersByTableName(tableName);
        redirectAttributes.addFlashAttribute("successMessage", message);

        return "redirect:/dashboard";
    }

}
