package com.diploma.airline_data_logger.service;

import com.diploma.airline_data_logger.dto.TableAuditDto;
import com.diploma.airline_data_logger.dto.TableSchemaDto;
import com.diploma.airline_data_logger.repository.DashboardRepository;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class DashboardServiceTest {

    private static String tableName;
    private static String auditTable;
    private static List<String> tableColumns;
    private static List<String> auditTableColumns;

    @Mock
    private DashboardRepository dashboardRepository;

    @InjectMocks
    private DashboardService underTest;

    @BeforeAll
    static void beforeAll() {
        tableName = "flights";
        auditTable = "audit_" + tableName;
        tableColumns = List.of("flight_id", "departure_time", "arrival_time", "origin", "destination");
        auditTableColumns = List.of("audit_id", "date_op", "code_op", "user_op", "host_op",
                "flight_id", "departure_time", "arrival_time", "origin", "destination",
                "departure_time_", "arrival_time_", "origin_", "destination_");
    }

    @Test
    void givenNothing_whenGetTableSchemas_thenReturnTableSchemaDtoList() {
        // given
        TableSchemaDto flights = new TableSchemaDto("flights", tableColumns,
                false, false);
        List<TableSchemaDto> tableSchemaDtoList = List.of(flights);

        given(dashboardRepository.getAllTableSchemas())
                .willReturn(tableSchemaDtoList);

        // when
        List<TableSchemaDto> result = underTest.getTableSchemas();

        // then
        assertThat(result).isEqualTo(tableSchemaDtoList);
        assertThat(result).hasSameSizeAs(tableSchemaDtoList);
    }

    @Test
    void givenTableName_whenGetAllAuditTableColumns_thenReturnColumnNamesList() {
        // given
        given(dashboardRepository.getAllAuditTableColumns(tableName))
                .willReturn(auditTableColumns);

        // when
        List<String> result = underTest.getAllAuditTableColumns(tableName);

        // then
        assertThat(result).isEqualTo(auditTableColumns);
        assertThat(result).hasSameSizeAs(auditTableColumns);
    }

    @Test
    void givenTableNameStartTimeAndEndTime_whenLoadDataFromAuditTable_thenReturnTableSchemaDtoList() {
        // given
        int id = 1;
        TableAuditDto tableAuditDto = new TableAuditDto(id, "2025-04-30 16:26:33", "I",
                "devuser@localhost", "DESKTOP-OBHTB9D", 1, new ArrayList<>(),
                List.of("2025-05-01 08:00:00", "2025-05-01 12:00:00", "LAX", "JFK"));
        String startTime = LocalDateTime.now().minusDays(1).toString();
        String endTime = LocalDateTime.now().plusDays(1).toString();

        List<TableAuditDto> tableAuditDtoList = List.of(tableAuditDto);

        given(dashboardRepository.loadDataFromAuditTable(tableName, startTime, endTime))
                .willReturn(tableAuditDtoList);

        // when
        List<TableAuditDto> result = underTest.loadDataFromAuditTable(tableName, startTime, endTime);

        // then
        assertThat(result).isEqualTo(tableAuditDtoList);
        assertThat(result).hasSameSizeAs(tableAuditDtoList);
    }

    @Test
    void givenTableName_whenAuditTableExists_thenReturnTrue() {
        // given
        given(dashboardRepository.doesAuditTableExist(tableName))
                .willReturn(true);

        // when
        boolean result = underTest.doesAuditTableExist(tableName);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void givenTableName_whenAuditTableDoesNotExist_thenReturnFalse() {
        // given
        given(dashboardRepository.doesAuditTableExist(tableName))
                .willReturn(false);

        // when
        boolean result = underTest.doesAuditTableExist(tableName);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void givenTableNameAndId_whenRestoreRecordInTable_thenReturnSuccessfulMessage() {
        // given
        int id = 1;
        given(dashboardRepository.restoreRecord(tableName, id))
                .willReturn(true);

        // when
        String result = underTest.restoreRecordInTable(tableName, id);

        // then
        assertThat(result)
                .isEqualTo("The record in the table '%s' successfully restored!".formatted(tableName));
    }

    @Test
    void givenTableNameAndId_whenRestoreRecordInTable_thenReturnThrowException() {
        // given
        int id = 1;
        given(dashboardRepository.restoreRecord(tableName, id))
                .willReturn(false);

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result =
                assertThatThrownBy(() -> underTest.restoreRecordInTable(tableName, id));

        // then
        result.isInstanceOf(IllegalStateException.class)
                .hasMessage("Something went wrong!");
    }

}
