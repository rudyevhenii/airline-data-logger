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

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
public class DashboardControllerIT {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TableAuditRepository tableAuditRepository;

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
        jdbcTemplate.execute("DELETE FROM %s".formatted(tableName));
    }

    @Test
    void itShouldRedirectToDashboardAndHttp302() throws Exception {
        mockMvc.perform(get("/")
                        .with(user(userDetails)))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    void itShouldReturnTableSchemasDtoListAndHttp200() throws Exception {
        mockMvc.perform(get("/dashboard")
                        .with(user(userDetails)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attributeExists("tableSchemas"));
    }

    @Test
    void itShouldRedirectToDashboardAndHttp302IfAuditTableDoesNotExist() throws Exception {
        String startTime = LocalDateTime.now().minusDays(2).toString();
        String endTime = LocalDateTime.now().minusDays(1).toString();

        mockMvc.perform(get("/dashboard/table-audit/{tableName}" +
                        "?startTime={startTime}&endTime={endTime}", tableName, startTime, endTime)
                        .with(user(userDetails)))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    void itShouldLoadDataFromAuditTableAndHttp200() throws Exception {
        String startTime = LocalDateTime.now().minusDays(2).toString();
        String endTime = LocalDateTime.now().minusDays(1).toString();

        mockMvc.perform(get("/tables/create-log-table/{tableName}", tableName)
                        .with(user(userDetails)))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"))
                .andExpect(flash().attributeExists("successMessage"));

        mockMvc.perform(get("/dashboard/table-audit/{tableName}" +
                        "?startTime={startTime}&endTime={endTime}", tableName, startTime, endTime)
                        .with(user(userDetails)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("audit-table"))
                .andExpect(model().attributeExists("recordCount", "tableName",
                        "tableAuditList", "tableAuditColumns"));
    }

    @Test
    void itShouldRestoreRecordInTableAndHttp302() throws Exception {
        int id = 1;
        int restoreId = 2;

        String insertRecord = """
                INSERT INTO %s (departure_time, arrival_time, origin, destination)
                VALUES ('2025-05-01 08:00:00', '2025-05-01 12:00:00', 'LAX', 'BOS');
                """.formatted(tableName);
        String deleteRecord = "DELETE FROM %s WHERE flight_id = %d;".formatted(tableName, id);

        mockMvc.perform(get("/tables/create-log-table/{tableName}", tableName)
                        .with(user(userDetails)))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"))
                .andExpect(flash().attributeExists("successMessage"));

        tableAuditRepository.createTriggersForTable(tableName);

        jdbcTemplate.execute(insertRecord);
        jdbcTemplate.execute(deleteRecord);

        mockMvc.perform(get("/dashboard/restore/{tableName}?id={id}", tableName, restoreId)
                        .with(user(userDetails)))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("successMessage"))
                .andExpect(redirectedUrl("/dashboard/table-audit/%s".formatted(tableName)));
    }

}
