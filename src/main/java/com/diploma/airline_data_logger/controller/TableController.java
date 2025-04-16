package com.diploma.airline_data_logger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tables/view")
public class TableController {

    @GetMapping("/{tableName}")
    public String getCriticalTableLogs(@PathVariable String tableName, Model model) {
        model.addAttribute("tableName", tableName);
        return "table";
    }

}
