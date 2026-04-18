package com.diploma.airline_data_logger.service;

import com.diploma.airline_data_logger.repository.TableAuditRepository;
import com.diploma.airline_data_logger.repository.TableMetadataProvider;
import com.diploma.airline_data_logger.service.impl.TableAuditServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TableAuditServiceTest {

    @Mock
    private TableAuditRepository tableAuditRepository;

    @Mock
    private TableMetadataProvider tableMetadataProvider;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private TableAuditServiceImpl underTest;

    @Test
    @DisplayName("Should successfully create triggers when validation passes")
    void createTriggersForTable_Success() {
        String tableName = "bookings";
        String auditTableName = "audit_bookings";

        given(tableMetadataProvider.doesTableExist(auditTableName)).willReturn(true);
        given(tableMetadataProvider.doTriggersExistForTable(tableName)).willReturn(false);

        String result = underTest.createTriggersForTable(tableName);

        assertThat(result).isEqualTo("Triggers for table 'bookings' successfully created!");
        verify(tableAuditRepository, times(1)).createTriggersForTable(tableName);
    }

    @Test
    @DisplayName("Should throw exception when audit table does not exist")
    void createTriggersForTable_Fail_NoAuditTable() {
        String tableName = "flights";
        String auditTableName = "audit_flights";

        given(tableMetadataProvider.doesTableExist(auditTableName)).willReturn(false);

        assertThatThrownBy(() -> underTest.createTriggersForTable(tableName))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Audit table should be created first!");

        verify(tableAuditRepository, never()).createTriggersForTable(anyString());
    }

    @Test
    @DisplayName("Should throw exception when triggers already exist")
    void createTriggersForTable_Fail_TriggersAlreadyExist() {
        String tableName = "passengers";
        String auditTableName = "audit_passengers";

        given(tableMetadataProvider.doesTableExist(auditTableName)).willReturn(true);
        given(tableMetadataProvider.doTriggersExistForTable(tableName)).willReturn(true);

        assertThatThrownBy(() -> underTest.createTriggersForTable(tableName))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Triggers for table 'passengers' already exist!");

        verify(tableAuditRepository, never()).createTriggersForTable(anyString());
    }

}