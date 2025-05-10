package com.diploma.airline_data_logger.controller;

import com.diploma.airline_data_logger.repository.TableAuditRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
public class TableAuditControllerIT {

    @Autowired
    private TableAuditRepository tableAuditRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserDetailsService userDetailsService;

    private UserDetails userDetails;

    @Value("${application.database.name}")
    private String databaseName;

    private String tableName;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        tableName = "flights";
        userDetails = userDetailsService.loadUserByUsername("admintest@gmail.com");
    }

    @AfterEach
    void tearDown() {
        String sql = """
                SELECT table_name FROM information_schema.tables
                WHERE table_schema = '%s'
                AND table_name LIKE 'audit_%%';
                """.formatted(databaseName);

        List<String> tableNames = jdbcTemplate.queryForList(sql, String.class);

        for (String name : tableNames) {
            jdbcTemplate.execute("DROP TABLE IF EXISTS %s".formatted(name));
        }
        tableAuditRepository.deleteTriggersForTable(tableName);
    }

    @Test
    void itShouldCreateAuditTableAndRedirectToDashboard() throws Exception {
        itShouldAcceptUriTemplateAndRedirectToDashboard("/tables/create-log-table/{tableName}");
    }

    private void itShouldAcceptUriTemplateAndRedirectToDashboard(String uriTemplate) throws Exception {
        mockMvc.perform(get(uriTemplate, tableName)
                        .with(user(userDetails)))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("successMessage"))
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    void itShouldDeleteAuditTableAndRedirectToDashboard() throws Exception {
        itShouldAcceptUriTemplateAndRedirectToDashboard("/tables/create-log-table/{tableName}");

        itShouldAcceptUriTemplateAndRedirectToDashboard("/tables/delete-log-table/{tableName}");
    }

    @Test
    void itShouldCreateTriggersForTableAndRedirectToDashboard() throws Exception {
        itShouldAcceptUriTemplateAndRedirectToDashboard("/tables/create-log-table/{tableName}");

        itShouldAcceptUriTemplateAndRedirectToDashboard("/tables/create-triggers/{tableName}");
    }

    @Test
    void itShouldDeleteTriggersByTableNameAndRedirectToDashboard() throws Exception {
        itShouldAcceptUriTemplateAndRedirectToDashboard("/tables/create-log-table/{tableName}");

        itShouldAcceptUriTemplateAndRedirectToDashboard("/tables/create-triggers/{tableName}");

        itShouldAcceptUriTemplateAndRedirectToDashboard("/tables/delete-triggers/{tableName}");
    }

}
