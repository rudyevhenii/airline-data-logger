package com.diploma.airline_data_logger.repository.impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableMetadataProviderImplTest {

    private static String tableName;
    private static String auditTable;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private TableMetadataProviderImpl underTest;

    @BeforeAll
    static void beforeAll() {
        tableName = "flights";
        auditTable = "audit_" + tableName;
    }

    @Test
    void givenNothing_whenQueryForList_thenReturnAllTableNames() {
        // given
        List<String> tableNames = List.of("bookings", "crew", "crew_assignments",
                "crew_roles", "flights", "passengers");

        given(jdbcTemplate.queryForList(anyString(), eq(String.class))).willReturn(tableNames);

        // when
        List<String> result = underTest.getAllTableNames();

        // then
        assertThat(result).isEqualTo(tableNames);
        assertThat(result.size()).isEqualTo(tableNames.size());
    }

    @Test
    void givenTableName_whenQueryForList_thenReturnAllColumnsForTable() {
        // given
        List<String> columns = List.of("flight_id", "departure_time", "arrival_time", "origin", "destination");
        given(jdbcTemplate.queryForList(anyString(), eq(String.class), eq(tableName)))
                .willReturn(columns);

        // when
        List<String> result = underTest.getAllColumnsForTable(tableName);

        // then
        assertThat(result).isEqualTo(columns);
        assertThat(result.size()).isEqualTo(columns.size());
    }

    @Test
    void givenTableName_whenQueryForList_thenReturnAllColumnsDataType() {
        // given
        List<String> dataTypes = List.of("int", "datetime", "datetime", "varchar", "varchar");
        given(jdbcTemplate.queryForList(anyString(), eq(String.class), eq(tableName)))
                .willReturn(dataTypes);

        // when
        List<String> result = underTest.getAllColumnsDataType(tableName);

        // then
        assertThat(result).isEqualTo(dataTypes);
        assertThat(result.size()).isEqualTo(dataTypes.size());
    }

    @Test
    void givenTableName_whenQueryForObject_thenReturnTrueIfAuditTableExists() {
        // given
        given(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(auditTable)))
                .willReturn(1);

        // when
        boolean result = underTest.doesAuditTableExist(tableName);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void givenTableName_whenQueryForObject_thenReturnFalseIfAuditTableDoesNotExist() {
        // given
        given(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(auditTable)))
                .willReturn(0);

        // when
        boolean result = underTest.doesAuditTableExist(tableName);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void givenTableName_whenQueryForObject_thenReturnTrueIfTriggersForTableExist() {
        // given
        given(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(tableName)))
                .willReturn(3);

        // when
        boolean result = underTest.doTriggersExistForTable(tableName);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void givenTableName_whenQueryForObject_thenReturnFalseIfTriggersForTableDoesNotExist() {
        // given
        given(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(tableName)))
                .willReturn(0);

        // when
        boolean result = underTest.doTriggersExistForTable(tableName);

        // then
        assertThat(result).isFalse();
    }

}
