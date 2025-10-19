INSERT INTO roles (name) VALUES ('ADMIN');

INSERT INTO employees (email, password, role_id) VALUES
('admintest@gmail.com', '{bcrypt}$2a$12$J2XfNoXxJef/SmVuvXduBeWYsadtysvjKAPfj17BfiRiuBf.V7DHq', 1); --admintest
