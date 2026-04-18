-- Table that contains crew members
CREATE TABLE IF NOT EXISTS `crew` (
    `crew_id` INT PRIMARY KEY AUTO_INCREMENT,
    `first_name` VARCHAR(100) NOT NULL,
    `last_name` VARCHAR(100) NOT NULL,
    `email` VARCHAR(100) NOT NULL,
    `phone` VARCHAR(100) NOT NULL,
    `role_id` INT NOT NULL,
    CONSTRAINT FK_CrewCrewRole FOREIGN KEY (`role_id`) REFERENCES `crew_roles`(`role_id`)
);
