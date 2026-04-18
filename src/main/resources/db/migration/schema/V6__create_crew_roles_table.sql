-- Table that stores crew roles
CREATE TABLE IF NOT EXISTS `crew_roles` (
    `role_id` INT PRIMARY KEY AUTO_INCREMENT,
    `role_name` VARCHAR(50) NOT NULL,
    `description` TEXT
);
