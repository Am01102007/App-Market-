/**
 * Script de creación del esquema de base de datos para AppMarket - MySQL.
 * 
 * Este archivo define la estructura completa de la base de datos del sistema
 * de marketplace específicamente para MySQL, incluyendo todas las tablas, 
 * relaciones, índices y restricciones necesarias.
 * 
 * @author Sistema App Market
 * @version 1.0
 * @since 2024
 * @database MySQL 8.0+
 */

-- =====================================================
-- TABLA: market_place
-- =====================================================
CREATE TABLE IF NOT EXISTS market_place (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Identificador único del marketplace',
    nombre VARCHAR(100) NOT NULL COMMENT 'Nombre del marketplace',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha de última actualización'
) COMMENT = 'Tabla principal del marketplace que agrupa todos los productos';

-- =====================================================
-- TABLA: categories
-- =====================================================
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Identificador único de la categoría',
    name VARCHAR(100) NOT NULL UNIQUE COMMENT 'Nombre de la categoría',
    description TEXT COMMENT 'Descripción detallada de la categoría',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha de última actualización'
) COMMENT = 'Categorías para clasificar y organizar productos';

-- =====================================================
-- TABLA: users
-- =====================================================
CREATE TABLE IF NOT EXISTS users (
    id BINARY(16) PRIMARY KEY COMMENT 'Identificador único UUID del usuario',
    username VARCHAR(50) UNIQUE COMMENT 'Nombre de usuario único',
    nombre VARCHAR(100) NOT NULL COMMENT 'Nombre completo del usuario',
    cedula VARCHAR(20) NOT NULL UNIQUE COMMENT 'Número de cédula único',
    direccion VARCHAR(200) COMMENT 'Dirección de residencia',
    email VARCHAR(100) NOT NULL UNIQUE COMMENT 'Correo electrónico único',
    password VARCHAR(255) NOT NULL COMMENT 'Contraseña encriptada',
    first_name VARCHAR(100) COMMENT 'Primer nombre',
    last_name VARCHAR(100) COMMENT 'Apellido',
    address VARCHAR(200) COMMENT 'Dirección alternativa',
    role VARCHAR(20) DEFAULT 'USER' COMMENT 'Rol del usuario',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha de última actualización',
    
    INDEX idx_users_email (email),
    INDEX idx_users_cedula (cedula),
    INDEX idx_users_username (username)
) COMMENT = 'Usuarios registrados en el sistema';

-- =====================================================
-- TABLA: products
-- =====================================================
CREATE TABLE IF NOT EXISTS products (
    id BINARY(16) PRIMARY KEY COMMENT 'Identificador único UUID del producto',
    name VARCHAR(200) NOT NULL COMMENT 'Nombre del producto',
    image_url VARCHAR(500) COMMENT 'URL de la imagen del producto',
    description TEXT COMMENT 'Descripción detallada del producto',
    price DECIMAL(10,2) NOT NULL COMMENT 'Precio del producto',
    status ENUM('AVAILABLE', 'SOLD', 'RESERVED') DEFAULT 'AVAILABLE' COMMENT 'Estado del producto',
    user_id BINARY(16) NOT NULL COMMENT 'ID del usuario vendedor',
    category_id BIGINT NOT NULL COMMENT 'ID de la categoría',
    market_place_id BIGINT NOT NULL COMMENT 'ID del marketplace',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha de última actualización',
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE,
    FOREIGN KEY (market_place_id) REFERENCES market_place(id) ON DELETE CASCADE,
    
    INDEX idx_products_user (user_id),
    INDEX idx_products_category (category_id),
    INDEX idx_products_marketplace (market_place_id),
    INDEX idx_products_price (price),
    INDEX idx_products_status (status)
) COMMENT = 'Productos disponibles en el marketplace';

-- =====================================================
-- TABLA: orders
-- =====================================================
CREATE TABLE IF NOT EXISTS orders (
    id BINARY(16) PRIMARY KEY COMMENT 'Identificador único UUID de la orden',
    user_id BINARY(16) NOT NULL COMMENT 'ID del usuario comprador',
    total_amount DECIMAL(10,2) NOT NULL COMMENT 'Monto total de la orden',
    status ENUM('PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED') DEFAULT 'PENDING' COMMENT 'Estado de la orden',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha de última actualización',
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    
    INDEX idx_orders_user (user_id),
    INDEX idx_orders_status (status),
    INDEX idx_orders_created (created_at)
) COMMENT = 'Órdenes de compra realizadas por usuarios';

-- =====================================================
-- TABLA: payments
-- =====================================================
CREATE TABLE IF NOT EXISTS payments (
    id BINARY(16) PRIMARY KEY COMMENT 'Identificador único UUID del pago',
    order_id BINARY(16) NOT NULL COMMENT 'ID de la orden pagada',
    user_id BINARY(16) NOT NULL COMMENT 'ID del usuario que paga',
    amount DECIMAL(10,2) NOT NULL COMMENT 'Monto del pago',
    payment_method VARCHAR(50) COMMENT 'Método de pago utilizado',
    status ENUM('PENDING', 'COMPLETED', 'FAILED', 'REFUNDED') DEFAULT 'PENDING' COMMENT 'Estado del pago',
    transaction_id VARCHAR(100) COMMENT 'ID de transacción externa',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación',
    
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    
    INDEX idx_payments_order (order_id),
    INDEX idx_payments_user (user_id),
    INDEX idx_payments_status (status)
) COMMENT = 'Pagos realizados para las órdenes';

-- =====================================================
-- TABLA: product_comments
-- Descripción: Comentarios y reseñas de productos
-- =====================================================
CREATE TABLE IF NOT EXISTS product_comments (
    id BINARY(16) PRIMARY KEY COMMENT 'Identificador único UUID del comentario',
    comment TEXT NOT NULL COMMENT 'Texto del comentario',
    product_id BINARY(16) NOT NULL COMMENT 'ID del producto comentado',
    user_id BINARY(16) NOT NULL COMMENT 'ID del usuario que comenta',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha de última actualización',
    
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    
    INDEX idx_comments_product (product_id),
    INDEX idx_comments_user (user_id)
) COMMENT = 'Comentarios y reseñas de productos por parte de usuarios';

-- =====================================================
-- TABLA: product_likes
-- =====================================================
CREATE TABLE IF NOT EXISTS product_likes (
    id BINARY(16) PRIMARY KEY COMMENT 'Identificador único UUID del like',
    product_id BINARY(16) NOT NULL COMMENT 'ID del producto',
    user_id BINARY(16) NOT NULL COMMENT 'ID del usuario que da like',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación',
    
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    
    UNIQUE KEY unique_user_product_like (user_id, product_id),
    INDEX idx_likes_product (product_id),
    INDEX idx_likes_user (user_id)
) COMMENT = 'Sistema de "me gusta" para productos';

-- =====================================================
-- TABLA: contacts
-- =====================================================
CREATE TABLE IF NOT EXISTS contacts (
    id BINARY(16) PRIMARY KEY COMMENT 'Identificador único UUID del contacto',
    user_id BINARY(16) NOT NULL COMMENT 'ID del usuario propietario',
    contact_user_id BINARY(16) NOT NULL COMMENT 'ID del usuario contacto',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación',
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (contact_user_id) REFERENCES users(id) ON DELETE CASCADE,
    
    UNIQUE KEY unique_user_contact (user_id, contact_user_id),
    INDEX idx_contacts_user (user_id),
    INDEX idx_contacts_contact_user (contact_user_id)
) COMMENT = 'Sistema de contactos entre usuarios del marketplace';

-- =====================================================
-- TABLA: chats
-- =====================================================
CREATE TABLE IF NOT EXISTS chats (
    id BINARY(16) PRIMARY KEY COMMENT 'Identificador único UUID del chat',
    name VARCHAR(100) COMMENT 'Nombre del chat (opcional)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha de última actualización',
    
    INDEX idx_chats_created (created_at)
) COMMENT = 'Salas de chat para comunicación entre usuarios';

-- =====================================================
-- TABLA: chat_participants
-- =====================================================
CREATE TABLE IF NOT EXISTS chat_participants (
    id BINARY(16) PRIMARY KEY COMMENT 'Identificador único UUID del participante',
    chat_id BINARY(16) NOT NULL COMMENT 'ID del chat',
    user_id BINARY(16) NOT NULL COMMENT 'ID del usuario participante',
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de unión al chat',
    
    FOREIGN KEY (chat_id) REFERENCES chats(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    
    UNIQUE KEY unique_chat_user (chat_id, user_id),
    INDEX idx_participants_chat (chat_id),
    INDEX idx_participants_user (user_id)
) COMMENT = 'Participantes de cada sala de chat';

-- =====================================================
-- TABLA: messages
-- =====================================================
CREATE TABLE IF NOT EXISTS messages (
    id BINARY(16) PRIMARY KEY COMMENT 'Identificador único UUID del mensaje',
    content TEXT NOT NULL COMMENT 'Contenido del mensaje',
    chat_id BINARY(16) NOT NULL COMMENT 'ID del chat donde se envía',
    user_id BINARY(16) NOT NULL COMMENT 'ID del usuario que envía',
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha y hora de envío',
    
    FOREIGN KEY (chat_id) REFERENCES chats(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    
    INDEX idx_messages_chat (chat_id),
    INDEX idx_messages_user (user_id),
    INDEX idx_messages_sent_at (sent_at)
) COMMENT = 'Mensajes enviados en las salas de chat';

-- =====================================================
-- TABLA: order_items
-- =====================================================
CREATE TABLE IF NOT EXISTS order_items (
    id BINARY(16) PRIMARY KEY COMMENT 'Identificador único UUID del item',
    order_id BINARY(16) NOT NULL COMMENT 'ID de la orden',
    product_id BINARY(16) NOT NULL COMMENT 'ID del producto',
    quantity INT NOT NULL DEFAULT 1 COMMENT 'Cantidad del producto',
    unit_price DECIMAL(10,2) NOT NULL COMMENT 'Precio unitario al momento de la compra',
    total_price DECIMAL(10,2) NOT NULL COMMENT 'Precio total del item',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación',
    
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    
    INDEX idx_order_items_order (order_id),
    INDEX idx_order_items_product (product_id)
) COMMENT = 'Items individuales de cada orden de compra';

-- Insertar datos iniciales
INSERT IGNORE INTO market_place (nombre) VALUES ('AppMarket Principal');

INSERT IGNORE INTO categories (name, description) VALUES 
('Electrónicos', 'Dispositivos electrónicos y tecnología'),
('Ropa', 'Vestimenta y accesorios'),
('Hogar', 'Artículos para el hogar'),
('Deportes', 'Equipamiento deportivo'),
('Libros', 'Literatura y material educativo');