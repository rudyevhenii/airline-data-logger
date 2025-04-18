CREATE TRIGGER after_insert_booking
AFTER INSERT ON bookings
FOR EACH ROW
BEGIN
    INSERT INTO audit_bookings (
        date_op, code_op, user_op, host_op,
        booking_id, flight_id_, passenger_id_, seat_number_, booking_time_
    ) VALUES (
        NOW(), 'I', USER(), @@hostname, NEW.booking_id,
        NEW.flight_id, NEW.passenger_id, NEW.seat_number, NEW.booking_time
    );
END;

CREATE TRIGGER after_update_booking
AFTER UPDATE ON bookings
FOR EACH ROW
BEGIN
    INSERT INTO audit_bookings (
        date_op, code_op, user_op, host_op,
        booking_id, flight_id, passenger_id, seat_number, booking_time,
        flight_id_, passenger_id_, seat_number_, booking_time_
    ) VALUES (
        NOW(), 'U', USER(), @@hostname,
        OLD.booking_id, OLD.flight_id, OLD.passenger_id, OLD.seat_number, OLD.booking_time,
        NEW.flight_id, NEW.passenger_id, NEW.seat_number, NEW.booking_time
    );
END;

CREATE TRIGGER after_delete_booking
AFTER DELETE ON bookings
FOR EACH ROW
BEGIN
    INSERT INTO audit_bookings (
        date_op, code_op, user_op, host_op,
        booking_id, flight_id, passenger_id, seat_number, booking_time
    ) VALUES (
        NOW(), 'D', USER(), @@hostname, OLD.booking_id,
        OLD.flight_id, OLD.passenger_id, OLD.seat_number, OLD.booking_time
    );
END;