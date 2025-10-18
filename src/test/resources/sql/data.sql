INSERT INTO roles (name) VALUES ('ADMIN');

INSERT INTO employees (email, password, role_id) VALUES
('admin1@gmail.com', '{bcrypt}$2a$12$lsd1S5xPv/t0kqo5x6rDYejq7QPIVT6.tCmTHEg4uTslB2GA4wxoi', 1); --admin1
