package com.diploma.airline_data_logger.service;

import com.diploma.airline_data_logger.repository.TableAuditRepository;
import com.diploma.airline_data_logger.repository.TableMetadataProvider;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
public class TableAuditServiceTest {

    private static String tableName;
    private static String auditTable;

    @Mock
    private TableAuditRepository tableAuditRepository;

    @Mock
    private TableMetadataProvider tableMetadataProvider;

    @InjectMocks
    private TableAuditService underTest;

    @BeforeAll
    static void beforeAll() {
        tableName = "flights";
        auditTable = "audit_" + tableName;
    }

    @Test
    void givenTableName_whenCreateAuditTableByTableName_thenReturnSuccessfulMessage() {
        // given
        given(tableMetadataProvider.doesTableExist(auditTable))
                .willReturn(false);
        willDoNothing().given(tableAuditRepository)
                .createAuditTable(tableName);

        // when
        String result = underTest.createAuditTableByTableName(tableName);

        // then
        assertThat(result).isEqualTo("'%s' table successfully created!".formatted(auditTable));
    }

    @Test
    void givenTableName_whenCreateAuditTableByTableName_thenThrowsException() {
        // given
        given(tableMetadataProvider.doesTableExist(auditTable))
                .willReturn(true);

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result =
                assertThatThrownBy(() -> underTest.createAuditTableByTableName(tableName));

        // then
        result.isInstanceOf(IllegalStateException.class)
                .hasMessage("'%s' table already exists.".formatted(auditTable));

    }

    @Test
    void givenTableName_whenCreateTriggersForTable_thenThrowExceptionIfAuditTableDoesNotExist() {
        // given
        given(tableMetadataProvider.doesTableExist(auditTable))
                .willReturn(false);

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result =
                assertThatThrownBy(() -> underTest.createTriggersForTable(tableName));

        // then
        result.isInstanceOf(IllegalStateException.class)
                .hasMessage("Audit table should be created first!");
    }

    @Test
    void givenTableName_whenCreateTriggersForTable_thenThrowExceptionIfTriggersAlreadyExistForTable() {
        // given
        given(tableMetadataProvider.doesTableExist(auditTable))
                .willReturn(true);
        given(tableMetadataProvider.doTriggersExistForTable(tableName))
                .willReturn(true);

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result =
                assertThatThrownBy(() -> underTest.createTriggersForTable(tableName));

        // then
        result.isInstanceOf(IllegalStateException.class)
                .hasMessage("Triggers for table '%s' already exist!".formatted(tableName));
    }

    @Test
    void givenTableName_whenCreateTriggersForTable_thenReturnSuccessfulMessage() {
        // given
        given(tableMetadataProvider.doesTableExist(auditTable))
                .willReturn(true);
        given(tableMetadataProvider.doTriggersExistForTable(tableName))
                .willReturn(false);
        willDoNothing().given(tableAuditRepository).createTriggersForTable(tableName);

        // when
        String result = underTest.createTriggersForTable(tableName);

        // then
        assertThat(result).isEqualTo("Triggers for table '%s' successfully created!".formatted(tableName));
    }

    @Test
    void givenTableName_whenDeleteTriggersByTableName_thenThrowExceptionIfAuditTableDoesNotExist() {
        // given
        given(tableMetadataProvider.doesTableExist(auditTable))
                .willReturn(false);

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result =
                assertThatThrownBy(() -> underTest.deleteTriggersByTableName(tableName));

        // then
        result.isInstanceOf(IllegalStateException.class)
                .hasMessage("Audit table should be created first!");
    }

    @Test
    void givenTableName_whenDeleteTriggersByTableName_thenThrowExceptionIfTriggersAlreadyExistForTable() {
        // given
        given(tableMetadataProvider.doesTableExist(auditTable))
                .willReturn(true);
        given(tableMetadataProvider.doTriggersExistForTable(tableName))
                .willReturn(false);

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result =
                assertThatThrownBy(() -> underTest.deleteTriggersByTableName(tableName));

        // then
        result.isInstanceOf(IllegalStateException.class)
                .hasMessage("Triggers for table '%s' do not exist!".formatted(tableName));
    }

    @Test
    void givenTableName_whenDeleteTriggersByTableName_thenReturnSuccessfulMessage() {
        // given
        given(tableMetadataProvider.doesTableExist(auditTable))
                .willReturn(true);
        given(tableMetadataProvider.doTriggersExistForTable(tableName))
                .willReturn(true);
        willDoNothing().given(tableAuditRepository).deleteTriggersForTable(tableName);

        // when
        String result = underTest.deleteTriggersByTableName(tableName);

        // then
        assertThat(result).isEqualTo("Triggers for table '%s' successfully deleted!".formatted(tableName));
    }

    @Test
    void givenTableName_whenDeleteAuditTableByTableName_thenThrowExceptionIfAuditTableDoesNotExist() {
        // given
        given(tableMetadataProvider.doesTableExist(auditTable))
                .willReturn(false);

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result =
                assertThatThrownBy(() -> underTest.deleteAuditTableByTableName(tableName));

        // then
        result.isInstanceOf(IllegalStateException.class)
                .hasMessage("Audit table should be created first!");
    }

    @Test
    void givenTableName_whenDeleteAuditTableByTableName_thenReturnSuccessfulMessage() {
        // given
        given(tableMetadataProvider.doesTableExist(auditTable))
                .willReturn(true);
        given(tableMetadataProvider.doTriggersExistForTable(tableName))
                .willReturn(true);
        willDoNothing().given(tableAuditRepository).deleteTriggersForTable(tableName);
        willDoNothing().given(tableAuditRepository).deleteAuditTable(tableName);

        // when
        String result = underTest.deleteAuditTableByTableName(tableName);

        // then
        assertThat(result).isEqualTo("'%s' table successfully deleted!".formatted(auditTable));
    }


}
