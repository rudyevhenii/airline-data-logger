USE `airline_data_logger`;

INSERT INTO `employees` (`email`, `password`, `role`) VALUES
('admin1@gmail.com', '{bcrypt}$2a$12$.PXouG/7MUi74kk6L15hAOz0gVHl/gO97KGnjvjGaU7jW7pHXh8t2', 'ADMIN'), -- admin1
('admin2@gmail.com', '{bcrypt}$2a$12$ZsaqGqcW9tVoJ1odgr5gl.2W/FYOiNfpP0NqVB9oXC0VQYuPlVWLC', 'ADMIN'), -- admin2
('admin3@gmail.com', '{bcrypt}$2a$12$WB9A58IG01xG850kpANwUOHqBQZOBwXjpc0ykicogH4p7/mUtvlvy', 'ADMIN') -- admin3
