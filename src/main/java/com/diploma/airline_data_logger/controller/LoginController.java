package com.diploma.airline_data_logger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping(value = "/login")
    public String getLoginPage(@RequestParam(name = "error", required = false) boolean hasError,
                               @RequestParam(name = "logout", required = false) String logout,
                               Model model) {
        if (hasError) {
            model.addAttribute("errorMessage", "Email or Password is incorrect!");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "You have successfully logged out!");
        }
        return "login";
    }

}
