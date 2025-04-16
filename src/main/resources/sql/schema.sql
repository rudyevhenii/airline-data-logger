-- Users creation in the database
CREATE SCHEMA IF NOT EXISTS `airline_data_logger`;

USE `airline_data_logger`;

-- Employees table
CREATE TABLE IF NOT EXISTS `employees`(
	`employee_id` INT AUTO_INCREMENT PRIMARY KEY,
    `email` VARCHAR(100) NOT NULL UNIQUE,
    `password` VARCHAR(100) NOT NULL,
    `role` VARCHAR(100) NOT NULL,
);

-- The whole airline db system that should be logged
-- Table that stores flight information
CREATE TABLE `flights` (
    `flight_id` INT PRIMARY KEY AUTO_INCREMENT,
    `departure_time` DATETIME NOT NULL,
    `arrival_time` DATETIME NOT NULL,
    `origin` VARCHAR(3) NOT NULL,
    `destination` VARCHAR(3) NOT NULL
);

-- Table that stores passenger information
CREATE TABLE `passengers` (
    `passenger_id` INT PRIMARY KEY AUTO_INCREMENT,
    `first_name` VARCHAR(100) NOT NULL,
    `last_name` VARCHAR(100) NOT NULL,
    `email` VARCHAR(100),
    `passport_no` VARCHAR(20)
);

-- Table that stores flight bookings made by passengers
CREATE TABLE `bookings` (
    `booking_id` INT PRIMARY KEY AUTO_INCREMENT,
    `flight_id` INT NOT NULL,
    `passenger_id` INT NOT NULL,
    `seat_number` VARCHAR(10),
    `booking_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT FK_BookingFlight FOREIGN KEY (`flight_id`) REFERENCES `flights`(`flight_id`),
    CONSTRAINT FK_BookingPassenger FOREIGN KEY (`passenger_id`) REFERENCES `passengers`(`passenger_id`)
);

-- Table that stores crew roles
CREATE TABLE `crew_roles` (
    `role_id` INT PRIMARY KEY AUTO_INCREMENT,
    `role_name` VARCHAR(50) NOT NULL,
    `description` TEXT
);

-- Table that contains crew members
CREATE TABLE `crew` (
    `crew_id` INT PRIMARY KEY AUTO_INCREMENT,
    `first_name` VARCHAR(100) NOT NULL,
    `last_name` VARCHAR(100) NOT NULL,
    `email` VARCHAR(100) NOT NULL,
    `phone` VARCHAR(100) NOT NULL,
    `role_id` INT NOT NULL,
    CONSTRAINT FK_CrewCrewRole FOREIGN KEY (`role_id`) REFERENCES `crew_roles`(`role_id`)
);

-- Table that assigns crew members to specific flights
CREATE TABLE `crew_assignments` (
    `assignment_id` INT PRIMARY KEY AUTO_INCREMENT,
    `flight_id` INT NOT NULL,
    `crew_id` INT NOT NULL,
    CONSTRAINT FK_CrewAssignmentFlight FOREIGN KEY (`flight_id`) REFERENCES `flights`(`flight_id`),
    CONSTRAINT FK_CrewAssignmentCrew FOREIGN KEY (`crew_id`) REFERENCES `crew`(`crew_id`)
);