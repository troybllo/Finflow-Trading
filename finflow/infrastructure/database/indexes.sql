CREATE INDEX idx_portfolio_user_id ON portfolios(user_id);
CREATE INDEX idx_portfolio_name ON portfolios(user_id, name);
-- Porfolio views
CREATE INDEX idx_holdings_portflio_id ON holdings(portfolio_id);
CREATE INDEX idx_holdings_symbol ON holdings(portfolio_id, symbol);
CREATE INDEX idx_holdings_symbols ON holdings(symbol);
-- Order history 
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_status ON orders(user_id, status);
CREATE INDEX idx_orders_symbol ON orders(user_id, symbol); 
CREATE INDEX idx_orders_time_range ON orders(created_at);
-- Order Execution details
CREATE INDEX idx_transactions_order_id ON transactions(order_id);
CREATE INDEX idx_transactions_symbol ON transactions(symbol);
CREATE INDEX idx_trasactions_time_range ON transactions(executed_at);


