-- The whole airline db system that should be logged
-- Table that stores flight information
CREATE TABLE IF NOT EXISTS `flights` (
    `flight_id` INT PRIMARY KEY AUTO_INCREMENT,
    `departure_time` DATETIME NOT NULL,
    `arrival_time` DATETIME NOT NULL,
    `origin` VARCHAR(3) NOT NULL,
    `destination` VARCHAR(3) NOT NULL
);
