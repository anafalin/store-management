SET REFERENTIAL_INTEGRITY = 0;
TRUNCATE TABLE users RESTART IDENTITY;
TRUNCATE TABLE clients RESTART IDENTITY;
TRUNCATE TABLE orders RESTART IDENTITY;
TRUNCATE TABLE products RESTART IDENTITY;
SET REFERENTIAL_INTEGRITY = 1;


INSERT INTO users ( activation_code, email, login, password) values
('123456789', 'user@mail.ru', 'user', '123');

INSERT INTO roles (id, name) VALUES (1, 'USER');

INSERT INTO carts (id, amount) VALUES (1, 20000);

INSERT INTO clients(firstname, lastname, cart_id, user_id) VALUES
('User Firstname', 'User LastName', 1, 1);

INSERT INTO products (count, name, description, price) VALUES
(5, 'Laptop', 'Description laptop', 15000),
(5, 'Intel i-5', 'Description Intel i-5', 10000),
(5, 'Keyboard', 'Description Keyboard', 5000);

INSERT INTO cart_rows (count, cart_id, product_id, amount) VALUES
(1, 1, 1, 15000),
(1, 1, 3, 5000);

INSERT INTO orders (amount, status, client_id) VALUES
(10000, 'NEW', 1);

INSERT INTO order_rows (amount, count, order_id, product_id) VALUES
(10000, 1, 1, 2);