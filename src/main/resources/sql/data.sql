USE `airline_data_logger`;

INSERT INTO `roles` (`role`)
VALUES ('ADMIN'), ('USER');

INSERT INTO `employees` (`email`, `password`, `role_id`) VALUES
('admin1@gmail.com', '{bcrypt}$2a$12$.PXouG/7MUi74kk6L15hAOz0gVHl/gO97KGnjvjGaU7jW7pHXh8t2', 1), -- admin1
('user1@gmail.com', '{bcrypt}$2a$12$KL4ppHbIQ/xaxlmZL06y/ujr4Qg7Ykk3AjjCS6mmfqOGkTH1xax/2', 2), -- user1
('user2@gmail.com', '{bcrypt}$2a$12$djCgDowx8gD5z1L.9Ie7C..jkyvP5usEUZM92HD2ta7JdiNzAnMGK', 2), -- user2
('user3@gmail.com', '{bcrypt}$2a$12$AYkm4CazQ.go.X.2sz3G2eaDr8AVHqg6jxZtG.7ywU3nurjdWPiWa', 2); -- user3

SELECT e.id, e.email, e.password, r.role FROM employees e
JOIN roles r ON e.role_id = r.role_id;

TRUNCATE TABLE `employees`;