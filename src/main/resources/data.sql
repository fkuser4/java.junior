MERGE INTO buyer (buyer_id, first_name, last_name, title) KEY (buyer_id) VALUES
    (1, 'Jabba', 'Hutt', 'the'),
    (2, 'Anakin', 'Skywalker', NULL),
    (3, 'Jar Jar', 'Binks', NULL),
    (4, 'Han', 'Solo', NULL),
    (5, 'Leia', 'Organa', 'Princess');

MERGE INTO buyer_address (buyer_address_id, city, street, home_number) KEY (buyer_address_id) VALUES
    (1, 'Zagreb', 'Ilica', '10'),
    (2, 'Split', 'Riva', '5');

MERGE INTO orders (
    order_id, buyer_id, order_status, order_time, payment_option,
    delivery_address_id, contact_number, note, currency, total_price
    ) KEY (order_id) VALUES
    (1, 1, 'WAITING_FOR_CONFIRMATION', CURRENT_TIMESTAMP, 'CASH', 1, '+38591111222', 'bez luka', 'EUR', 24.50),
    (2, 2, 'PREPARING', CURRENT_TIMESTAMP, 'CARD_UPFRONT', 2, '+38598123456', NULL, 'EUR', 41.00);

MERGE INTO order_item (order_item_id, order_id, item_nr, name, quantity, price) KEY (order_item_id) VALUES
    (1, 1, 1, 'Pizza Margherita', 1, 9.50),
    (2, 1, 2, 'Coca-Cola', 3, 5.00),
    (3, 2, 1, 'Burger', 2, 12.00),
    (4, 2, 2, 'Pomfrit', 1, 5.00),
    (5, 2, 3, 'Sok', 2, 6.00);

ALTER TABLE buyer ALTER COLUMN buyer_id RESTART WITH 6;
ALTER TABLE buyer_address ALTER COLUMN buyer_address_id RESTART WITH 3;
ALTER TABLE orders ALTER COLUMN order_id RESTART WITH 3;
ALTER TABLE order_item ALTER COLUMN order_item_id RESTART WITH 6;