package com.diploma.airline_data_logger.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
public class LoginControllerIT {

    private String tableName;

    private String email;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void itShouldReturnLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void itShouldRedirectIfEmailOrPasswordIsIncorrect() throws Exception {
        boolean hasError = true;

        mockMvc.perform(get("/login?error={hasError}", hasError))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("errorMessage"));
    }

    @Test
    void itShouldReturnLoginPageIfLogout() throws Exception {
        boolean logout = true;

        mockMvc.perform(get("/login?logout={logout}", logout))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("successMessage"))
                .andExpect(view().name("login"));
    }

}
