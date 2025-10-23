/**
 * Script de datos de prueba para AppMarket.
 * 
 * Este archivo contiene datos de ejemplo para poblar la base de datos
 * durante el desarrollo y testing. Incluye usuarios de prueba, productos,
 * categorías y relaciones básicas para demostrar la funcionalidad completa
 * del sistema.
 * 
 * IMPORTANTE: Este archivo solo debe ejecutarse en entornos de desarrollo
 * y testing. NO debe usarse en producción.
 * 
 * Datos incluidos:
 * - Usuarios de prueba con diferentes roles
 * - Categorías de productos
 * - Productos de ejemplo
 * - Contactos entre usuarios
 * - Comentarios y likes de prueba
 * 
 * @author Sistema App Market
 * @version 1.0
 * @since 2024
 * @environment development, testing
 */

-- =====================================================
-- USUARIOS DE PRUEBA
-- Descripción: Usuarios con diferentes roles para testing
-- Contraseñas: todas son "password123" (encriptadas con BCrypt)
-- =====================================================

-- Usuario Administrador
INSERT IGNORE INTO users (id, username, email, password, first_name, last_name, cedula, address, role, nombre, direccion) VALUES 
(UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440000', '-', '')), 
 'admin', 
 'admin@appmarket.com', 
 '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM5lIX.P8yPkKNbTJ6Ca', 
 'Administrador', 
 'Sistema', 
 '12345678', 
 'Calle Principal 123, Ciudad', 
 'ADMIN',
 'Administrador Sistema',
 'Calle Principal 123, Ciudad');

-- Usuario Vendedor 1
INSERT IGNORE INTO users (id, username, email, password, first_name, last_name, cedula, address, role, nombre, direccion) VALUES 
(UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440001', '-', '')), 
 'juan_vendedor', 
 'juan@email.com', 
 '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM5lIX.P8yPkKNbTJ6Ca', 
 'Juan Carlos', 
 'Pérez', 
 '87654321', 
 'Avenida Comercial 456, Ciudad', 
 'USER',
 'Juan Carlos Pérez',
 'Avenida Comercial 456, Ciudad');

-- Usuario Vendedor 2
INSERT IGNORE INTO users (id, username, email, password, first_name, last_name, cedula, address, role, nombre, direccion) VALUES 
(UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440002', '-', '')), 
 'maria_store', 
 'maria@email.com', 
 '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM5lIX.P8yPkKNbTJ6Ca', 
 'María Elena', 
 'González', 
 '11223344', 
 'Carrera 15 #25-30, Ciudad', 
 'USER',
 'María Elena González',
 'Carrera 15 #25-30, Ciudad');

-- Usuario Comprador
INSERT IGNORE INTO users (id, username, email, password, first_name, last_name, cedula, address, role, nombre, direccion) VALUES 
(UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440003', '-', '')), 
 'carlos_comprador', 
 'carlos@email.com', 
 '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM5lIX.P8yPkKNbTJ6Ca', 
 'Carlos Alberto', 
 'Rodríguez', 
 '55667788', 
 'Diagonal 20 #10-15, Ciudad', 
 'USER',
 'Carlos Alberto Rodríguez',
 'Diagonal 20 #10-15, Ciudad');

-- =====================================================
-- PRODUCTOS DE PRUEBA
-- Descripción: Productos de ejemplo en diferentes categorías
-- =====================================================

-- Productos de Electrónicos (Categoría ID: 1)
INSERT IGNORE INTO products (id, name, description, price, image_url, status, user_id, category_id, market_place_id) VALUES 
(UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440001', '-', '')), 
 'iPhone 13 Pro', 
 'Smartphone Apple iPhone 13 Pro de 128GB en excelente estado. Incluye cargador original y caja.', 
 2500000.00, 
 'https://example.com/images/iphone13pro.jpg', 
 'AVAILABLE', 
 UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440001', '-', '')), 
 1, 
 1);

INSERT IGNORE INTO products (id, name, description, price, image_url, status, user_id, category_id, market_place_id) VALUES 
(UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440002', '-', '')), 
 'Laptop Dell Inspiron', 
 'Laptop Dell Inspiron 15 con procesador Intel i5, 8GB RAM, 256GB SSD. Perfecta para trabajo y estudio.', 
 1800000.00, 
 'https://example.com/images/dell-laptop.jpg', 
 'AVAILABLE', 
 UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440002', '-', '')), 
 1, 
 1);

-- Productos de Ropa (Categoría ID: 2)
INSERT IGNORE INTO products (id, name, description, price, image_url, status, user_id, category_id, market_place_id) VALUES 
(UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440003', '-', '')), 
 'Chaqueta de Cuero', 
 'Chaqueta de cuero genuino, talla M, color negro. Estilo clásico y elegante.', 
 350000.00, 
 'https://example.com/images/leather-jacket.jpg', 
 'AVAILABLE', 
 UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440001', '-', '')), 
 2, 
 1);

INSERT IGNORE INTO products (id, name, description, price, image_url, status, user_id, category_id, market_place_id) VALUES 
(UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440004', '-', '')), 
 'Zapatos Deportivos Nike', 
 'Zapatos deportivos Nike Air Max, talla 42, color blanco con detalles azules. Muy cómodos.', 
 280000.00, 
 'https://example.com/images/nike-shoes.jpg', 
 'AVAILABLE', 
 UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440002', '-', '')), 
 2, 
 1);

-- Productos de Hogar (Categoría ID: 3)
INSERT IGNORE INTO products (id, name, description, price, image_url, status, user_id, category_id, market_place_id) VALUES 
(UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440005', '-', '')), 
 'Sofá de 3 Puestos', 
 'Sofá cómodo de 3 puestos, tapizado en tela gris. Ideal para sala de estar.', 
 850000.00, 
 'https://example.com/images/sofa-3-seats.jpg', 
 'AVAILABLE', 
 UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440001', '-', '')), 
 3, 
 1);

-- Producto Vendido (para demostrar diferentes estados)
INSERT IGNORE INTO products (id, name, description, price, image_url, status, user_id, category_id, market_place_id) VALUES 
(UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440006', '-', '')), 
 'Bicicleta de Montaña', 
 'Bicicleta de montaña marca Trek, aro 26, 21 velocidades. Excelente para aventuras.', 
 650000.00, 
 'https://example.com/images/mountain-bike.jpg', 
 'SOLD', 
 UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440002', '-', '')), 
 4, 
 1);

-- =====================================================
-- CONTACTOS ENTRE USUARIOS
-- Descripción: Relaciones de contacto para demostrar la funcionalidad social
-- =====================================================

-- Juan tiene como contacto a María
INSERT IGNORE INTO contacts (id, user_id, contact_user_id) VALUES 
(UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440001', '-', '')), 
 UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440001', '-', '')), 
 UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440002', '-', '')));

-- María tiene como contacto a Juan
INSERT IGNORE INTO contacts (id, user_id, contact_user_id) VALUES 
(UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440002', '-', '')), 
 UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440002', '-', '')), 
 UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440001', '-', '')));

-- Carlos tiene como contactos a Juan y María
INSERT IGNORE INTO contacts (id, user_id, contact_user_id) VALUES 
(UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440003', '-', '')), 
 UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440003', '-', '')), 
 UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440001', '-', '')));

INSERT IGNORE INTO contacts (id, user_id, contact_user_id) VALUES 
(UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440004', '-', '')), 
 UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440003', '-', '')), 
 UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440002', '-', '')));

-- =====================================================
-- COMENTARIOS DE PRODUCTOS
-- Descripción: Comentarios de ejemplo para demostrar el sistema de reseñas
-- =====================================================

-- Comentarios de productos
INSERT IGNORE INTO product_comments (id, comment, product_id, user_id) VALUES
(UNHEX(REPLACE('880e8400-e29b-41d4-a716-446655440001', '-', '')),
 'Excelente producto, muy recomendado!',
 UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440001', '-', '')),
 UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440003', '-', '')));

INSERT IGNORE INTO product_comments (id, comment, product_id, user_id) VALUES
(UNHEX(REPLACE('880e8400-e29b-41d4-a716-446655440002', '-', '')),
 'Buena calidad precio, llegó en perfectas condiciones.',
 UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440002', '-', '')),
 UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440001', '-', '')));

INSERT IGNORE INTO product_comments (id, comment, product_id, user_id) VALUES
(UNHEX(REPLACE('880e8400-e29b-41d4-a716-446655440003', '-', '')),
 'Me encanta este producto, superó mis expectativas.',
 UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440003', '-', '')),
 UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440002', '-', '')));

-- =====================================================
-- LIKES DE PRODUCTOS
-- Descripción: Sistema de "me gusta" para productos populares
-- =====================================================

-- Carlos le da like al iPhone
INSERT IGNORE INTO product_likes (id, product_id, user_id) VALUES 
(UNHEX(REPLACE('990e8400-e29b-41d4-a716-446655440001', '-', '')), 
 UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440001', '-', '')), 
 UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440003', '-', '')));

-- Carlos le da like a la laptop
INSERT IGNORE INTO product_likes (id, product_id, user_id) VALUES 
(UNHEX(REPLACE('990e8400-e29b-41d4-a716-446655440002', '-', '')), 
 UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440002', '-', '')), 
 UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440003', '-', '')));

-- María le da like a la chaqueta de cuero
INSERT IGNORE INTO product_likes (id, product_id, user_id) VALUES 
(UNHEX(REPLACE('990e8400-e29b-41d4-a716-446655440003', '-', '')), 
 UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440003', '-', '')), 
 UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440002', '-', '')));

-- =====================================================
-- CHAT DE PRUEBA
-- Descripción: Chat de ejemplo entre usuarios
-- =====================================================

-- Crear un chat entre Juan y Carlos
INSERT IGNORE INTO chats (id, name) VALUES 
(UNHEX(REPLACE('aa0e8400-e29b-41d4-a716-446655440001', '-', '')), 
 'Chat sobre iPhone 13 Pro');

-- Agregar participantes al chat
INSERT IGNORE INTO chat_participants (id, chat_id, user_id) VALUES 
(UNHEX(REPLACE('bb0e8400-e29b-41d4-a716-446655440001', '-', '')), 
 UNHEX(REPLACE('aa0e8400-e29b-41d4-a716-446655440001', '-', '')), 
 UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440001', '-', '')));

INSERT IGNORE INTO chat_participants (id, chat_id, user_id) VALUES 
(UNHEX(REPLACE('bb0e8400-e29b-41d4-a716-446655440002', '-', '')), 
 UNHEX(REPLACE('aa0e8400-e29b-41d4-a716-446655440001', '-', '')), 
 UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440003', '-', '')));

-- Mensajes del chat
INSERT IGNORE INTO messages (id, content, chat_id, user_id) VALUES 
(UNHEX(REPLACE('cc0e8400-e29b-41d4-a716-446655440001', '-', '')), 
 'Hola, me interesa el iPhone 13 Pro que tienes publicado', 
 UNHEX(REPLACE('aa0e8400-e29b-41d4-a716-446655440001', '-', '')), 
 UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440003', '-', '')));

INSERT IGNORE INTO messages (id, content, chat_id, user_id) VALUES 
(UNHEX(REPLACE('cc0e8400-e29b-41d4-a716-446655440002', '-', '')), 
 'Hola Carlos! Claro, está en excelente estado. ¿Te gustaría verlo?', 
 UNHEX(REPLACE('aa0e8400-e29b-41d4-a716-446655440001', '-', '')), 
 UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440001', '-', '')));

INSERT IGNORE INTO messages (id, content, chat_id, user_id) VALUES 
(UNHEX(REPLACE('cc0e8400-e29b-41d4-a716-446655440003', '-', '')), 
 'Perfecto, ¿cuándo podríamos encontrarnos?', 
 UNHEX(REPLACE('aa0e8400-e29b-41d4-a716-446655440001', '-', '')), 
 UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440003', '-', '')));

-- =====================================================
-- ORDEN DE PRUEBA
-- Descripción: Orden de compra de ejemplo con sus items y pago
-- =====================================================

-- Crear una orden para Carlos
INSERT IGNORE INTO orders (id, user_id, total_amount, status) VALUES 
(UNHEX(REPLACE('dd0e8400-e29b-41d4-a716-446655440001', '-', '')), 
 UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440003', '-', '')), 
 280000.00, 
 'CONFIRMED');

-- Item de la orden (zapatos deportivos)
INSERT IGNORE INTO order_items (id, order_id, product_id, quantity, unit_price, total_price) VALUES 
(UNHEX(REPLACE('ee0e8400-e29b-41d4-a716-446655440001', '-', '')), 
 UNHEX(REPLACE('dd0e8400-e29b-41d4-a716-446655440001', '-', '')), 
 UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440004', '-', '')), 
 1, 
 280000.00, 
 280000.00);

-- Pago de la orden
INSERT IGNORE INTO payments (id, order_id, user_id, amount, payment_method, status, transaction_id) VALUES 
(UNHEX(REPLACE('ff0e8400-e29b-41d4-a716-446655440001', '-', '')), 
 UNHEX(REPLACE('dd0e8400-e29b-41d4-a716-446655440001', '-', '')), 
 UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440003', '-', '')), 
 280000.00, 
 'Tarjeta de Crédito', 
 'COMPLETED', 
 'TXN_20240101_001');
