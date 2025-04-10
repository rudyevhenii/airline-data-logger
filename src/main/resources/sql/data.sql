USE `airline_data_logger`;

INSERT INTO `roles` (`role`)
VALUES ('ADMIN'), ('USER');

INSERT INTO `employees` (`email`, `password`, `role_id`)
VALUES ('admin1@gmail.com', '{noop}12345', 1),
('user1@gmail.com', '{noop}user1', 2),
('user2@gmail.com', '{noop}user2', 2),
('user3@gmail.com', '{noop}user3', 2);

SELECT e.id, e.email, e.password, r.role FROM employees e
JOIN roles r ON e.role_id = r.role_id;

TRUNCATE TABLE `employees`;