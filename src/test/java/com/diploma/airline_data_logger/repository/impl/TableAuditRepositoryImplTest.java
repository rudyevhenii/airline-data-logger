package com.diploma.airline_data_logger.repository.impl;

import com.diploma.airline_data_logger.repository.TableMetadataProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TableAuditRepositoryImplTest {

    private static String tableName;
    private static String auditTable;
    private static List<String> tableColumns;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private TableMetadataProvider tableMetadataProvider;

    @InjectMocks
    private TableAuditRepositoryImpl underTest;

    @BeforeAll
    static void beforeAll() {
        tableName = "flights";
        auditTable = "audit_" + tableName;
        tableColumns = List.of("flight_id", "departure_time", "arrival_time", "origin", "destination");
    }

    @Test
    void givenTableName_whenCreateAuditTable_thenReturnNothing() {
        // given
        List<String> tableDataTypes = List.of("int", "datetime", "datetime", "varchar", "varchar");

        given(tableMetadataProvider.getAllColumnsForTable(tableName)).willReturn(tableColumns);
        given(tableMetadataProvider.getAllColumnsDataType(tableName)).willReturn(tableDataTypes);
        willDoNothing().given(jdbcTemplate).execute(anyString());

        // when
        underTest.createAuditTable(tableName);

        // then
        verify(jdbcTemplate).execute(anyString());
        verify(tableMetadataProvider).getAllColumnsForTable(tableName);
        verify(tableMetadataProvider).getAllColumnsDataType(tableName);
    }

    @Test
    void givenTableName_whenDeleteAuditTable_thenReturnNothing() {
        // given
        willDoNothing().given(jdbcTemplate).execute(anyString());

        // when
        underTest.deleteAuditTable(tableName);

        // then
        verify(jdbcTemplate).execute(anyString());
    }

    @Test
    void givenTableName_whenCreateTriggersForTable_thenReturnNothing() {
        // given
        List<String> auditTableColumns = List.of("audit_id", "date_op", "code_op", "user_op",
                "host_op", "flight_id_", "departure_time_",
                "arrival_time_", "origin_", "destination_");

        willDoNothing().given(jdbcTemplate).execute(anyString());
        given(tableMetadataProvider.getAllColumnsForTable(tableName)).willReturn(tableColumns);
        given(tableMetadataProvider.getAllColumnsForTable(auditTable)).willReturn(auditTableColumns);

        // when
        underTest.createTriggersForTable(tableName);

        // then
        verify(jdbcTemplate, times(3)).execute(anyString());
        verify(tableMetadataProvider, times(6)).getAllColumnsForTable(anyString());
    }

    @Test
    void givenTableName_whenDeleteTriggersForTable_thenReturnNothing() {
        // given
        willDoNothing().given(jdbcTemplate).execute(anyString());

        // when
        underTest.deleteTriggersForTable(tableName);

        // then
        verify(jdbcTemplate, times(3)).execute(anyString());
    }

}
