CREATE SCHEMA IF NOT EXISTS `airline_data_logger`;

USE `airline_data_logger`;

CREATE TABLE IF NOT EXISTS `roles` (
	`role_id` INT AUTO_INCREMENT PRIMARY KEY,
    `role` VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS `employees`(
	`id` INT AUTO_INCREMENT PRIMARY KEY,
    `email` VARCHAR(255) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `role_id` INT NOT NULL,
    FOREIGN KEY (`role_id`) REFERENCES roles(`role_id`)
);