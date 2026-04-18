-- Table that stores passenger information
CREATE TABLE IF NOT EXISTS `passengers` (
    `passenger_id` INT PRIMARY KEY AUTO_INCREMENT,
    `first_name` VARCHAR(100) NOT NULL,
    `last_name` VARCHAR(100) NOT NULL,
    `email` VARCHAR(100),
    `passport_no` VARCHAR(20)
);
