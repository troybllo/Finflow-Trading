-- Seed data for testing indexes and queries
-- This file populates the database with realistic test data

-- Insert test users
INSERT INTO users (id, email, username, created_at) VALUES
(1, 'alice@test.com', 'alice', NOW() - INTERVAL '6 months'),
(2, 'bob@test.com', 'bob', NOW() - INTERVAL '4 months'),
(3, 'charlie@test.com', 'charlie', NOW() - INTERVAL '2 months');

-- Insert test portfolios
INSERT INTO portfolios (id, user_id, name, created_at) VALUES
(1, 1, 'Alice Main Portfolio', NOW() - INTERVAL '6 months'),
(2, 1, 'Alice Crypto Portfolio', NOW() - INTERVAL '5 months'),
(3, 2, 'Bob Trading Account', NOW() - INTERVAL '4 months'),
(4, 3, 'Charlie Conservative Fund', NOW() - INTERVAL '2 months');

-- Insert test holdings
INSERT INTO holdings (id, portfolio_id, symbol, quantity, average_cost) VALUES
(1, 1, 'AAPL', 10.00000000, 150.50000000),
(2, 1, 'GOOGL', 5.00000000, 2800.00000000),
(3, 1, 'TSLA', 15.00000000, 225.75000000),
(4, 2, 'BTC', 0.50000000, 45000.00000000),
(5, 2, 'ETH', 2.00000000, 3200.00000000),
(6, 3, 'AAPL', 20.00000000, 145.00000000),
(7, 3, 'MSFT', 12.00000000, 380.00000000),
(8, 4, 'SPY', 50.00000000, 450.00000000),
(9, 4, 'VTI', 30.00000000, 220.00000000);

-- Insert test orders (various statuses and time ranges)
INSERT INTO orders (id, user_id, symbol, side, order_type, quantity, price, status, created_at, filled_at) VALUES
-- Alice's orders
(1, 1, 'AAPL', 'buy', 'limit', 10.00000000, 150.00000000, 'filled', NOW() - INTERVAL '6 months', NOW() - INTERVAL '6 months'),
(2, 1, 'GOOGL', 'buy', 'market', 5.00000000, NULL, 'filled', NOW() - INTERVAL '5 months', NOW() - INTERVAL '5 months'),
(3, 1, 'TSLA', 'buy', 'limit', 15.00000000, 225.00000000, 'filled', NOW() - INTERVAL '4 months', NOW() - INTERVAL '4 months'),
(4, 1, 'BTC', 'buy', 'market', 0.50000000, NULL, 'filled', NOW() - INTERVAL '3 months', NOW() - INTERVAL '3 months'),
(5, 1, 'ETH', 'buy', 'limit', 2.00000000, 3200.00000000, 'filled', NOW() - INTERVAL '2 months', NOW() - INTERVAL '2 months'),
(6, 1, 'AAPL', 'sell', 'limit', 5.00000000, 175.00000000, 'pending', NOW() - INTERVAL '1 day', NULL),
(7, 1, 'TSLA', 'buy', 'limit', 5.00000000, 220.00000000, 'pending', NOW() - INTERVAL '2 hours', NULL),

-- Bob's orders
(8, 2, 'AAPL', 'buy', 'market', 20.00000000, NULL, 'filled', NOW() - INTERVAL '4 months', NOW() - INTERVAL '4 months'),
(9, 2, 'MSFT', 'buy', 'limit', 12.00000000, 380.00000000, 'filled', NOW() - INTERVAL '3 months', NOW() - INTERVAL '3 months'),
(10, 2, 'AAPL', 'sell', 'limit', 10.00000000, 160.00000000, 'cancelled', NOW() - INTERVAL '2 months', NULL),
(11, 2, 'MSFT', 'buy', 'limit', 5.00000000, 375.00000000, 'pending', NOW() - INTERVAL '3 days', NULL),

-- Charlie's orders
(12, 3, 'SPY', 'buy', 'market', 50.00000000, NULL, 'filled', NOW() - INTERVAL '2 months', NOW() - INTERVAL '2 months'),
(13, 3, 'VTI', 'buy', 'limit', 30.00000000, 220.00000000, 'filled', NOW() - INTERVAL '1 month', NOW() - INTERVAL '1 month'),
(14, 3, 'SPY', 'buy', 'limit', 10.00000000, 448.00000000, 'pending', NOW() - INTERVAL '5 hours', NULL);

-- Insert test transactions (executions for filled orders)
INSERT INTO transactions (id, order_id, symbol, side, quantity, price, executed_at) VALUES
-- Order 1 - AAPL buy (single fill)
(1, 1, 'AAPL', 'buy', 10.00000000, 150.50000000, NOW() - INTERVAL '6 months'),

-- Order 2 - GOOGL buy (partial fills)
(2, 2, 'GOOGL', 'buy', 3.00000000, 2795.00000000, NOW() - INTERVAL '5 months'),
(3, 2, 'GOOGL', 'buy', 2.00000000, 2810.00000000, NOW() - INTERVAL '5 months' + INTERVAL '5 minutes'),

-- Order 3 - TSLA buy (single fill)
(4, 3, 'TSLA', 'buy', 15.00000000, 225.75000000, NOW() - INTERVAL '4 months'),

-- Order 4 - BTC buy (partial fills)
(5, 4, 'BTC', 'buy', 0.30000000, 44500.00000000, NOW() - INTERVAL '3 months'),
(6, 4, 'BTC', 'buy', 0.20000000, 45500.00000000, NOW() - INTERVAL '3 months' + INTERVAL '10 minutes'),

-- Order 5 - ETH buy (single fill)
(7, 5, 'ETH', 'buy', 2.00000000, 3200.00000000, NOW() - INTERVAL '2 months'),

-- Order 8 - AAPL buy (Bob)
(8, 8, 'AAPL', 'buy', 20.00000000, 145.00000000, NOW() - INTERVAL '4 months'),

-- Order 9 - MSFT buy (Bob - partial fills)
(9, 9, 'MSFT', 'buy', 7.00000000, 378.00000000, NOW() - INTERVAL '3 months'),
(10, 9, 'MSFT', 'buy', 5.00000000, 382.00000000, NOW() - INTERVAL '3 months' + INTERVAL '15 minutes'),

-- Order 12 - SPY buy (Charlie)
(11, 12, 'SPY', 'buy', 50.00000000, 450.00000000, NOW() - INTERVAL '2 months'),

-- Order 13 - VTI buy (Charlie)
(12, 13, 'VTI', 'buy', 30.00000000, 220.00000000, NOW() - INTERVAL '1 month');

-- Reset sequences to avoid conflicts with future auto-generated IDs
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
SELECT setval('portfolios_id_seq', (SELECT MAX(id) FROM portfolios));
SELECT setval('holdings_id_seq', (SELECT MAX(id) FROM holdings));
SELECT setval('orders_id_seq', (SELECT MAX(id) FROM orders));
SELECT setval('transactions_id_seq', (SELECT MAX(id) FROM transactions));
