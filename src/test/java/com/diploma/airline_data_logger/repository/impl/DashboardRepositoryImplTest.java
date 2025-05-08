package com.diploma.airline_data_logger.repository.impl;

import com.diploma.airline_data_logger.dto.TableAuditDto;
import com.diploma.airline_data_logger.dto.TableSchemaDto;
import com.diploma.airline_data_logger.repository.TableMetadataProvider;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DashboardRepositoryImplTest {

    private static String tableName;
    private static String auditTable;
    private static List<String> tableNames;
    private static List<String> tableColumns;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private TableMetadataProvider tableMetadataProvider;

    @InjectMocks
    private DashboardRepositoryImpl underTest;

    @BeforeAll
    static void beforeAll() {
        tableName = "flights";
        auditTable = "audit_" + tableName;
        tableNames = List.of("bookings", "crew", "crew_assignments",
                "crew_roles", "flights", "passengers");
        tableColumns = List.of("flight_id", "departure_time", "arrival_time", "origin", "destination");
    }

    @Test
    void givenNothing_whenGetAllTableSchemas_thenTableSchemaDtoListIfAuditTableFalseAndTriggersFalse() {
        // given
        given(tableMetadataProvider.getAllTableNames())
                .willReturn(tableNames);
        given(tableMetadataProvider.getAllColumnsForTable(anyString()))
                .willReturn(new ArrayList<>());
        given(tableMetadataProvider.doesAuditTableExist(anyString()))
                .willReturn(false);
        given(tableMetadataProvider.doTriggersExistForTable(anyString()))
                .willReturn(false);

        // when
        List<TableSchemaDto> result = underTest.getAllTableSchemas();

        // then
        assertThat(result).hasSameSizeAs(tableNames);
        verify(tableMetadataProvider, times(1)).getAllTableNames();
        verify(tableMetadataProvider, times(tableNames.size())).getAllColumnsForTable(anyString());
        verify(tableMetadataProvider, times(tableNames.size())).doesAuditTableExist(anyString());
        verify(tableMetadataProvider, times(tableNames.size())).doTriggersExistForTable(anyString());
    }

    @Test
    void givenTableName_whenGetAllAuditTableColumns_thenReturnAuditTableColumnsList() {
        // given
        given(tableMetadataProvider.getAllColumnsForTable(eq(auditTable)))
                .willReturn(tableColumns);

        // when
        List<String> result = underTest.getAllAuditTableColumns(tableName);

        // then
        assertThat(result).isEqualTo(tableColumns);
    }

    @Test
    void givenTableName_whenQuery_thenReturnTableAuditDtoList() {
        // given
        List<String> auditTableColumns = List.of("audit_id", "date_op", "code_op", "user_op", "host_op",
                "flight_id", "departure_time", "arrival_time", "origin", "destination",
                "departure_time_", "arrival_time_", "origin_", "destination_");
        String startTime = LocalDateTime.now().minusDays(1).toString();
        String endTime = LocalDateTime.now().plusDays(1).toString();

        given(tableMetadataProvider.getAllColumnsForTable(eq(auditTable)))
                .willReturn(auditTableColumns);
        given(tableMetadataProvider.getAllColumnsForTable(eq(tableName)))
                .willReturn(tableColumns);
        given(jdbcTemplate.query(anyString(), any(RowMapper.class)))
                .willReturn(anyList());

        // when
        List<TableAuditDto> result = underTest.loadDataFromAuditTable(tableName, startTime, endTime);

        // then
        verify(tableMetadataProvider, times(2)).getAllColumnsForTable(anyString());
        verify(jdbcTemplate).query(anyString(), any(RowMapper.class));
    }

    @Test
    void givenTableName_whenAuditTableExists_thenReturnTrue() {
        // given
        given(tableMetadataProvider.doesAuditTableExist(eq(tableName)))
                .willReturn(true);

        // when
        boolean result = underTest.doesAuditTableExist(tableName);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void givenTableName_whenAuditTableDoesNotExist_thenReturnFalse() {
        // given
        given(tableMetadataProvider.doesAuditTableExist(eq(tableName)))
                .willReturn(false);

        // when
        boolean result = underTest.doesAuditTableExist(tableName);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void givenTableNameAndId_whenRestoreRecord_thenReturnTrue() {
        // given
        int id = 1;
        TableAuditDto tableAuditDto = new TableAuditDto(id, "2025-04-30 16:26:33", "I",
                "devuser@localhost", "DESKTOP-OBHTB9D", 1, new ArrayList<>(),
                List.of("2025-05-01 08:00:00", "2025-05-01 12:00:00", "LAX", "JFK"));

        given(tableMetadataProvider.getAllColumnsForTable(tableName))
                .willReturn(tableColumns);
        given(jdbcTemplate.update(anyString(), any(Object[].class)))
                .willReturn(1);
        given(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class)))
                .willReturn(tableAuditDto);

        // when
        boolean result = underTest.restoreRecord(tableName, id);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void givenTableNameAndId_whenRestoreRecord_thenReturnFalse() {
        // given
        int id = 1;
        TableAuditDto tableAuditDto = new TableAuditDto(id, "2025-04-30 16:26:33", "I",
                "devuser@localhost", "DESKTOP-OBHTB9D", 1, new ArrayList<>(),
                List.of("2025-05-01 08:00:00", "2025-05-01 12:00:00", "LAX", "JFK"));

        given(tableMetadataProvider.getAllColumnsForTable(tableName))
                .willReturn(tableColumns);
        given(jdbcTemplate.update(anyString(), any(Object[].class)))
                .willReturn(0);
        given(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class)))
                .willReturn(tableAuditDto);

        // when
        boolean result = underTest.restoreRecord(tableName, id);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void givenTableNameAndId_whenRestoreRecord_thenThrowException() {
        // given
        int id = 1;

        given(tableMetadataProvider.getAllColumnsForTable(tableName))
                .willReturn(tableColumns);
        given(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class)))
                .willReturn(null);

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(
                () -> underTest.restoreRecord(tableName, id));

        // then
        result.isInstanceOf(IllegalStateException.class)
                .hasMessage("Couldn't find a record with id: " + id);
    }

    @Test
    void givenTableNameAndId_whenFindRecordInAuditTableById_thenReturnOptionalOfTableAuditDto() {
        // given
        int id = 1;
        TableAuditDto tableAuditDto = new TableAuditDto(id, "2025-04-30 16:26:33", "I",
                "devuser@localhost", "DESKTOP-OBHTB9D", 1, new ArrayList<>(),
                List.of("2025-05-01 08:00:00", "2025-05-01 12:00:00", "LAX", "JFK"));

        given(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class)))
                .willReturn(tableAuditDto);

        // when
        Optional<TableAuditDto> result = underTest.findRecordInAuditTableById(tableName, id);

        // then
        assertThat(result).isEqualTo(Optional.ofNullable(tableAuditDto));
    }

}