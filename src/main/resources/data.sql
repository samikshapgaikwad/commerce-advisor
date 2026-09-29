-- Insert seed data for demo products
INSERT INTO products (sku, name, category, current_price, stock, reorder_threshold, demand_velocity, status) VALUES
('PRD-003', 'Organic Cotton T-Shirt', 'Apparel', 29.99, 8, 15, 12, 'LOW_STOCK'),
('PRD-008', 'Hoodie', 'Apparel', 59.99, 11, 12, 15, 'LOW_STOCK');