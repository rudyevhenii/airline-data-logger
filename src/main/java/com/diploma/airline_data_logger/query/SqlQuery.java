package com.diploma.airline_data_logger.query;

public interface SqlQuery {

    String UPSERT_RECORD_SQL = "INSERT INTO %s (%s) VALUES (%d, %s) AS %s ON DUPLICATE KEY UPDATE %s;";

    String SELECT_ALL_WHERE_SQL = "SELECT * FROM %s WHERE audit_id = %d;";

    String SELECT_ALL_TABLE_NAMES_SQL = """
            SELECT table_name
            FROM information_schema.TABLES
            WHERE table_schema = '%s'
            AND table_name <> 'employees'
            AND table_name <> 'roles'
            AND table_name <> 'flyway_schema_history'
            AND NOT table_name LIKE 'audit_%%';""";

    String SELECT_ALL_TABLE_COLUMNS_SQL = """
            SELECT column_name
            FROM information_schema.COLUMNS
            WHERE table_name = ?
            AND table_schema = '%s'
            ORDER BY ordinal_position;
            """;

    String SELECT_ALL_DATA_TYPE_COLUMNS_SQL = """
            SELECT data_type
            FROM information_schema.COLUMNS
            WHERE table_name = ?
            AND table_schema = '%s'
            ORDER BY ordinal_position;
            """;

    String SELECT_COUNT_TABLES_SQL = """
            SELECT COUNT(*)
            FROM information_schema.TABLES
            WHERE table_name = ?
            AND table_schema = '%s';
            """;

    String SELECT_COUNT_TABLE_TRIGGERS_SQL = """
            SELECT COUNT(*)
            FROM information_schema.TRIGGERS
            WHERE EVENT_OBJECT_TABLE = ?
            AND TRIGGER_SCHEMA = '%s';
            """;

    String CREATE_TABLE_IF_NOT_EXISTS_SQL = """
            CREATE TABLE IF NOT EXISTS %s (
                audit_id INT AUTO_INCREMENT PRIMARY KEY,
                date_op DATETIME NOT NULL,
                code_op CHAR(1) NOT NULL,
                user_op VARCHAR(100) NOT NULL,
                host_op VARCHAR(100) NOT NULL,
            %s
            );""";

    String DROP_TABLE_IF_EXISTS_SQL = "DROP TABLE IF EXISTS %s";

    String CREATE_TRIGGER_SQL = """
            CREATE TRIGGER after_%s_%s
            AFTER %s ON %s
            FOR EACH ROW
            BEGIN
                INSERT INTO %s (
                    %s%s
                ) VALUES (
                    NOW(), '%c', USER(), @@hostname, %s
                );
            END;""";

    String DROP_TRIGGER_IF_EXISTS = "DROP TRIGGER IF EXISTS `%s`.`%s`;";

    String SELECT_ALL_FROM_EMPLOYEES = """
            SELECT e.employee_id, e.email, e.password, r.name
            FROM employees e
            JOIN roles r ON e.role_id = r.role_id
            WHERE e.email = ?;""";
}
