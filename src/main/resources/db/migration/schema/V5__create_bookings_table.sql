-- Table that stores flight bookings made by passengers
CREATE TABLE IF NOT EXISTS `bookings` (
    `booking_id` INT PRIMARY KEY AUTO_INCREMENT,
    `flight_id` INT NOT NULL,
    `passenger_id` INT NOT NULL,
    `seat_number` VARCHAR(10),
    `booking_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT FK_BookingFlight FOREIGN KEY (`flight_id`) REFERENCES `flights`(`flight_id`),
    CONSTRAINT FK_BookingPassenger FOREIGN KEY (`passenger_id`) REFERENCES `passengers`(`passenger_id`)
);
