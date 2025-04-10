package com.diploma.airline_data_logger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping(value = "/login")
    public String getLoginPage(@RequestParam(name = "error", required = false) String error) {

        return "login";
    }
}
