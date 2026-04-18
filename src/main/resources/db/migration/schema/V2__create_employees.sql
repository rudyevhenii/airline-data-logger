-- Employees table
CREATE TABLE IF NOT EXISTS `employees` (
	`employee_id` INT AUTO_INCREMENT PRIMARY KEY,
    `email` VARCHAR(100) NOT NULL UNIQUE,
    `password` VARCHAR(100) NOT NULL,
    `role_id` INT NOT NULL,
    CONSTRAINT FK_EmployeeRole FOREIGN KEY (`role_id`) REFERENCES `roles`(`role_id`)
);
