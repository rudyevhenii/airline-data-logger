USE `airline_data_logger`;

INSERT INTO roles (name) VALUES ('ADMIN');

INSERT INTO employees (email, password, role_id) VALUES
('admin1@gmail.com', '{bcrypt}$2a$12$.PXouG/7MUi74kk6L15hAOz0gVHl/gO97KGnjvjGaU7jW7pHXh8t2', 1), -- admin1
('admin2@gmail.com', '{bcrypt}$2a$12$ZsaqGqcW9tVoJ1odgr5gl.2W/FYOiNfpP0NqVB9oXC0VQYuPlVWLC', 1), -- admin2
('admin3@gmail.com', '{bcrypt}$2a$12$WB9A58IG01xG850kpANwUOHqBQZOBwXjpc0ykicogH4p7/mUtvlvy', 1); -- admin3

INSERT INTO flights (departure_time, arrival_time, origin, destination) VALUES ('2025-05-01 08:00:00', '2025-05-01 12:00:00', 'LAX', 'JFK');
UPDATE flights SET destination = 'BOS' WHERE flight_id = 1;
INSERT INTO flights (departure_time, arrival_time, origin, destination) VALUES ('2025-06-15 14:00:00', '2025-06-15 18:30:00', 'ORD', 'ATL');
DELETE FROM flights WHERE flight_id = 2;
INSERT INTO flights (departure_time, arrival_time, origin, destination) VALUES ('2025-07-03 06:00:00', '2025-07-03 09:00:00', 'SEA', 'DEN');
UPDATE flights SET departure_time = '2025-07-03 07:00:00' WHERE flight_id = 3;
INSERT INTO flights (departure_time, arrival_time, origin, destination) VALUES ('2025-08-20 16:00:00', '2025-08-20 20:00:00', 'MIA', 'DFW');
UPDATE flights SET origin = 'PHX' WHERE flight_id = 4;
INSERT INTO flights (departure_time, arrival_time, origin, destination) VALUES ('2025-09-10 13:00:00', '2025-09-10 17:00:00', 'SFO', 'LAS');
UPDATE flights SET arrival_time = '2025-09-10 17:30:00' WHERE flight_id = 5;
INSERT INTO flights (departure_time, arrival_time, origin, destination) VALUES ('2025-10-01 10:00:00', '2025-10-01 14:00:00', 'JFK', 'LAX');
DELETE FROM flights WHERE flight_id = 1;
INSERT INTO flights (departure_time, arrival_time, origin, destination) VALUES ('2025-11-12 05:00:00', '2025-11-12 08:30:00', 'DEN', 'ORD');
UPDATE flights SET destination = 'SLC' WHERE flight_id = 6;
INSERT INTO flights (departure_time, arrival_time, origin, destination) VALUES ('2025-12-24 20:00:00', '2025-12-25 00:00:00', 'ATL', 'PHL');
UPDATE flights SET origin = 'IAD' WHERE flight_id = 7;
DELETE FROM flights WHERE flight_id = 4;
UPDATE flights SET departure_time = '2025-10-01 09:30:00' WHERE flight_id = 8;

INSERT INTO passengers (first_name, last_name, email, passport_no) VALUES ('John', 'Doe', 'john.doe@example.com', 'X12345678');
UPDATE passengers SET email = 'johnny.doe@example.com' WHERE passenger_id = 1;
INSERT INTO passengers (first_name, last_name, email, passport_no) VALUES ('Jane', 'Smith', 'jane.smith@example.com', 'Y98765432');
DELETE FROM passengers WHERE passenger_id = 2;
INSERT INTO passengers (first_name, last_name, email, passport_no) VALUES ('Alex', 'Brown', 'alex.brown@example.com', 'A56473829');
UPDATE passengers SET passport_no = 'A00000000' WHERE passenger_id = 1;
INSERT INTO passengers (first_name, last_name, email, passport_no) VALUES ('Emily', 'Davis', 'emily.davis@example.com', 'C11112222');
UPDATE passengers SET last_name = 'Johnson' WHERE passenger_id = 3;
INSERT INTO passengers (first_name, last_name, email, passport_no) VALUES ('Daniel', 'White', 'dan.white@example.com', 'Z34567890');
UPDATE passengers SET email = 'd.white@example.com' WHERE passenger_id = 4;
INSERT INTO passengers (first_name, last_name, email, passport_no) VALUES ('Sophia', 'Taylor', 'sophia.taylor@example.com', 'W22334455');
DELETE FROM passengers WHERE passenger_id = 1;
INSERT INTO passengers (first_name, last_name, email, passport_no) VALUES ('Michael', 'Lee', 'michael.lee@example.com', 'B99887766');
UPDATE passengers SET first_name = 'Mike' WHERE passenger_id = 5;

INSERT INTO bookings (flight_id, passenger_id, seat_number) VALUES (3, 3, '12A');
UPDATE bookings SET seat_number = '12B' WHERE booking_id = 1;
INSERT INTO bookings (flight_id, passenger_id, seat_number) VALUES (5, 4, '14C');
DELETE FROM bookings WHERE booking_id = 2;
INSERT INTO bookings (flight_id, passenger_id, seat_number) VALUES (6, 3, '15F');
UPDATE bookings SET flight_id = 5 WHERE booking_id = 3;
INSERT INTO bookings (flight_id, passenger_id, seat_number) VALUES (7, 5, '10A');
UPDATE bookings SET passenger_id = 5 WHERE booking_id = 1;
INSERT INTO bookings (flight_id, passenger_id, seat_number) VALUES (5, 5, '9B');
UPDATE bookings SET seat_number = '9A' WHERE booking_id = 4;
INSERT INTO bookings (flight_id, passenger_id, seat_number) VALUES (8, 3, '11C');
DELETE FROM bookings WHERE booking_id = 3;
INSERT INTO bookings (flight_id, passenger_id, seat_number) VALUES (6, 4, '8D');
UPDATE bookings SET seat_number = '8C' WHERE booking_id = 5;

INSERT INTO crew_roles (role_name, description) VALUES ('Pilot', 'Responsible for flying the aircraft');
UPDATE crew_roles SET description = 'Leads the flight crew and flies the plane' WHERE role_id = 1;
INSERT INTO crew_roles (role_name, description) VALUES ('Co-Pilot', 'Assists the pilot');
DELETE FROM crew_roles WHERE role_id = 2;
INSERT INTO crew_roles (role_name, description) VALUES ('Flight Attendant', 'Ensures passenger safety and comfort');
UPDATE crew_roles SET role_name = 'Senior Pilot' WHERE role_id = 1;
INSERT INTO crew_roles (role_name, description) VALUES ('Navigator', 'Assists with route planning and navigation');
UPDATE crew_roles SET description = 'Cabin crew, serves meals' WHERE role_id = 3;
INSERT INTO crew_roles (role_name, description) VALUES ('Engineer', 'Monitors aircraft systems');
UPDATE crew_roles SET role_name = 'Flight Engineer' WHERE role_id = 4;
INSERT INTO crew_roles (role_name, description) VALUES ('Purser', 'Leads the flight attendants');
DELETE FROM crew_roles WHERE role_id = 3;
INSERT INTO crew_roles (role_name, description) VALUES ('Dispatcher', 'Coordinates flights from the ground');
UPDATE crew_roles SET description = 'Handles flight dispatch and communications' WHERE role_id = 5;

INSERT INTO crew (first_name, last_name, email, phone, role_id) VALUES ('Alice', 'Walker', 'alice.walker@example.com', '555-1000', 1);
UPDATE crew SET phone = '555-2000' WHERE crew_id = 1;
INSERT INTO crew (first_name, last_name, email, phone, role_id) VALUES ('Bob', 'Johnson', 'bob.j@example.com', '555-1001', 4);
DELETE FROM crew WHERE crew_id = 2;
INSERT INTO crew (first_name, last_name, email, phone, role_id) VALUES ('Carol', 'Smith', 'carol.smith@example.com', '555-1002', 1);
UPDATE crew SET email = 'carol.s@example.com' WHERE crew_id = 1;
INSERT INTO crew (first_name, last_name, email, phone, role_id) VALUES ('David', 'Miller', 'david.m@example.com', '555-1003', 5);
UPDATE crew SET role_id = 5 WHERE crew_id = 3;
INSERT INTO crew (first_name, last_name, email, phone, role_id) VALUES ('Eva', 'Brown', 'eva.b@example.com', '555-1004', 1);
UPDATE crew SET first_name = 'Evelyn' WHERE crew_id = 4;
INSERT INTO crew (first_name, last_name, email, phone, role_id) VALUES ('Frank', 'Lopez', 'frank.l@example.com', '555-1005', 1);
DELETE FROM crew WHERE crew_id = 1;
UPDATE crew SET last_name = 'Lewis' WHERE crew_id = 3;

INSERT INTO crew_assignments (flight_id, crew_id) VALUES (5, 3);
UPDATE crew_assignments SET flight_id = 6 WHERE assignment_id = 1;
INSERT INTO crew_assignments (flight_id, crew_id) VALUES (6, 4);
DELETE FROM crew_assignments WHERE assignment_id = 2;
INSERT INTO crew_assignments (flight_id, crew_id) VALUES (7, 3);
UPDATE crew_assignments SET crew_id = 5 WHERE assignment_id = 1;
INSERT INTO crew_assignments (flight_id, crew_id) VALUES (8, 4);
UPDATE crew_assignments SET flight_id = 5 WHERE assignment_id = 3;
INSERT INTO crew_assignments (flight_id, crew_id) VALUES (6, 5);
DELETE FROM crew_assignments WHERE assignment_id = 3;
INSERT INTO crew_assignments (flight_id, crew_id) VALUES (7, 5);
UPDATE crew_assignments SET crew_id = 3 WHERE assignment_id = 1;
