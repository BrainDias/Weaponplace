-- Create the enum types for PostgreSQL
CREATE TYPE accessory_type AS ENUM ('LIGHT', 'MUZZLE_DEVICE', 'GRIP', 'SIGHT');
CREATE TYPE caliber AS ENUM ('7.62x39', '7.62x51', '7.62x54', '.45', '.50', '12.7x99', '9x19', '9x18', '5.56x45', '5.45x39');
CREATE TYPE feeding_type AS ENUM ('FEEDING_TYPE', 'DRUM', 'BOXED');
CREATE TYPE product_type AS ENUM ('WEAPON', 'ACCESSORY', 'AMMO', 'OILS_AND_LUBRICANTS');
CREATE TYPE sight_type AS ENUM ('Regular', 'Night', 'Thermal', 'Iron', 'RedDot');
CREATE TYPE weapon_type AS ENUM ('SNIPER_RIFLE', 'MACHINEGUN', 'PISTOL', 'ASSAULT_RIFLE');

-- Create the product table
CREATE TABLE products (
                         user_id BIGINT NOT NULL,  -- Внешний ключ на таблицу users
                         price NUMERIC,
                         name VARCHAR(255),
                         qty INT,
                         accessory_type accessory_type,
                         for_sale BOOLEAN,
                         hidden BOOLEAN,
                         image BYTEA,
                         product_type product_type,
                         magnification NUMERIC,
                         sight_type sight_type,
                         caliber caliber,
                         weight NUMERIC,
                         length INT,
                         accessories accessory_type,
                         rate_of_fire INT,
                         weapon_type weapon_type,
                         barrel_length INT,
                         feeding_type feeding_type,
                         ammo_type VARCHAR(255),
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         PRIMARY KEY (user_id, name),  -- Предполагаем, что имя продукта уникально для пользователя
                         CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Создаем таблицу для коллекции ProductFilter, связанной с таблицей users
CREATE TABLE product_filters (
                                      user_id BIGINT NOT NULL,  -- Внешний ключ на таблицу users
                                      product_type product_type,  -- Используем перечисление для типа продукта
                                      caliber caliber,  -- Используем перечисление для калибра
                                      accessory_type accessory_type,  -- Используем перечисление для типа аксессуаров
                                      max_price FLOAT,
                                      weapon_type weapon_type,  -- Используем перечисление для типа оружия
                                      name VARCHAR(255),
                                      PRIMARY KEY (user_id, name),  -- Уникальный фильтр на основе имени и пользователя
                                      CONSTRAINT fk_user_product_filter FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Создаем таблицу для заказов продуктов (ProductOrder)
CREATE TABLE product_orders (
                                id BIGSERIAL PRIMARY KEY,  -- Идентификатор заказа
                                buyer_id BIGINT NOT NULL,  -- Внешний ключ на таблицу users (покупатель)
                                seller_id BIGINT NOT NULL,  -- Внешний ключ на таблицу users (продавец)
                                price FLOAT,
                                delivered BOOLEAN,
                                confirmed BOOLEAN,
                                created_at TIMESTAMP,
                                updated_at TIMESTAMP,
                                CONSTRAINT fk_product_order_buyer FOREIGN KEY (buyer_id) REFERENCES users(id) ON DELETE CASCADE,
                                CONSTRAINT fk_product_order_seller FOREIGN KEY (seller_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Создаем таблицу для связи между заказами и продуктами
CREATE TABLE product_order_products (
                                        product_order_id BIGINT NOT NULL,  -- Внешний ключ на таблицу product_orders
                                        product_id BIGINT NOT NULL,  -- Внешний ключ на таблицу products
                                        PRIMARY KEY (product_order_id, product_id),
                                        CONSTRAINT fk_product_order_product_order FOREIGN KEY (product_order_id) REFERENCES product_orders(id) ON DELETE CASCADE,
                                        CONSTRAINT fk_product_order_product FOREIGN KEY (product_id) REFERENCES products(user_id,name) ON DELETE CASCADE
);

-- Создание таблицы для аукционов (Auction)
CREATE TABLE auctions (
                          id BIGSERIAL PRIMARY KEY,  -- Идентификатор аукциона
                          owner_id BIGINT NOT NULL,  -- Внешний ключ на таблицу users (владелец)
                          pretender_id BIGINT,  -- Внешний ключ на таблицу users (претендент)
                          closed BOOLEAN,
                          start_price FLOAT,
                          last_price FLOAT,
                          price_step FLOAT,
                          title VARCHAR(255),  -- Название аукциона
                          description TEXT,  -- Описание аукциона
                          created_at TIMESTAMP,
                          updated_at TIMESTAMP,
                          closing_at TIMESTAMP,
                          CONSTRAINT fk_auction_owner FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE,
                          CONSTRAINT fk_auction_pretender FOREIGN KEY (pretender_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Создание таблицы для связи между аукционами и продуктами
CREATE TABLE auction_products (
                                  auction_id BIGINT NOT NULL,  -- Внешний ключ на таблицу auctions
                                  product_id BIGINT NOT NULL,  -- Внешний ключ на таблицу products
                                  PRIMARY KEY (auction_id, product_id),
                                  CONSTRAINT fk_auction_product FOREIGN KEY (auction_id) REFERENCES auctions(id) ON DELETE CASCADE,
                                  CONSTRAINT fk_product_auction FOREIGN KEY (product_id) REFERENCES products(user_id,name) ON DELETE CASCADE
);

-- Таблица для связи пользователей и фильтров продуктов (UserProductFilters)
CREATE TABLE user_product_filters (
                                      user_id BIGINT NOT NULL,  -- Внешний ключ на таблицу users
                                      filter_id BIGINT NOT NULL,  -- Внешний ключ на таблицу product_filters
                                      PRIMARY KEY (user_id, filter_id),
                                      CONSTRAINT fk_user_filter FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                      CONSTRAINT fk_filter FOREIGN KEY (filter_id) REFERENCES product_filters(user_id,name) ON DELETE CASCADE
);

-- Таблица пользователей (User)
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,  -- Идентификатор пользователя
                       username VARCHAR(255) UNIQUE NOT NULL,  -- Имя пользователя
                       password VARCHAR(255) NOT NULL,  -- Пароль
                       rating FLOAT,  -- Рейтинг пользователя
                       ratings_number INTEGER,  -- Количество оценок
                       email VARCHAR(255),  -- Электронная почта
                       phone_number VARCHAR(50),  -- Номер телефона
                       active BOOLEAN,  -- Активен ли пользователь
                       order_history_hidden BOOLEAN,  -- Скрыта ли история заказов
                       avatar BYTEA,  -- Аватар пользователя
                       created_at TIMESTAMP,  -- Дата создания
                       updated_at TIMESTAMP,  -- Дата обновления
                       CONSTRAINT unique_email UNIQUE (email),  -- Уникальность email
                       CONSTRAINT unique_phone_number UNIQUE (phone_number)  -- Уникальность номера телефона
);

-- Таблица для хранения ролей пользователей (Authorities)
CREATE TABLE user_authorities (
                                  user_id BIGINT NOT NULL,  -- Внешний ключ на таблицу users
                                  authority VARCHAR(255) NOT NULL,  -- Роль или права пользователя
                                  PRIMARY KEY (user_id, authority),
                                  CONSTRAINT fk_user_authority FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Таблица для хранения продуктов пользователя
CREATE TABLE user_products (
                               user_id BIGINT NOT NULL,  -- Внешний ключ на таблицу users
                               product_id BIGINT NOT NULL,  -- Внешний ключ на таблицу products
                               PRIMARY KEY (user_id, product_id),
                               CONSTRAINT fk_user_product FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                               CONSTRAINT fk_product_user FOREIGN KEY (product_id) REFERENCES products(user_id,name) ON DELETE CASCADE
);

-- Таблица для хранения товаров в списке желаемого пользователя
CREATE TABLE user_wishlist (
                               user_id BIGINT NOT NULL,  -- Внешний ключ на таблицу users
                               product_id BIGINT NOT NULL,  -- Внешний ключ на таблицу products
                               PRIMARY KEY (user_id, product_id),
                               CONSTRAINT fk_user_wishlist FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                               CONSTRAINT fk_product_wishlist FOREIGN KEY (product_id) REFERENCES products(user_id,name) ON DELETE CASCADE
);

-- Таблица для хранения продуктов, о которых было уведомлено
CREATE TABLE user_wish_notified_products (
                                             user_id BIGINT NOT NULL,  -- Внешний ключ на таблицу users
                                             product_id BIGINT NOT NULL,  -- Внешний ключ на таблицу products
                                             PRIMARY KEY (user_id, product_id),
                                             CONSTRAINT fk_user_notified_product FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                             CONSTRAINT fk_notified_product FOREIGN KEY (product_id) REFERENCES products(user_id,name) ON DELETE CASCADE
);

--
INSERT INTO users (id, username, password, rating, ratings_number, email, phone_number, active, order_history_hidden, created_at, updated_at, avatar)
VALUES
    (1, 'user1', 'password1', 4.5, 10, 'user1@example.com', '+123456789', true, false, now(), now(), NULL),
    (2, 'user2', 'password2', 3.0, 20, 'user2@example.com', '+987654321', true, true, now(), now(), NULL),
    (3, 'admin', 'adminpassword', 5.0, 50, 'admin@example.com', '+1122334455', true, false, now(), now(), NULL);
INSERT INTO products (price, name, qty, accessory_type, for_sale, hidden, product_type, magnification, caliber, weight, length, rate_of_fire, weapon_type, barrel_length, feeding_type, ammo_type, created_at, updated_at,user_id)
VALUES
    (1000.00, 'Sniper Rifle X', 5, NULL, true, false, 'WEAPON', 4.0, 'RIFLE_NATO', 10.5, 120, 10, 'SNIPER_RIFLE', 30, 'DRUM', NULL, now(), now(),1),
    (150.00, '9mm Ammo', 1000, NULL, true, false, 'AMMO', NULL, 'PISTOL_NATO', NULL, NULL, NULL, NULL, NULL, NULL, '9x19', now(), now(),2),
    (250.00, 'Red Dot Sight', 50, 'SIGHT', true, false, 'ACCESSORY', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, now(), now(),3),
    (2000.00, 'Assault Rifle Y', 10, NULL, true, false, 'WEAPON', NULL, 'AR_NATO', 7.5, 90, 600, 'ASSAULT_RIFLE', NULL, 'BOXED', NULL, now(), now(), 1),
    (100.00, '5.56mm Ammo', 2000, NULL, true, false, 'AMMO', NULL, 'AR_NATO', NULL, NULL, NULL, NULL, NULL, NULL, '5.56x45', now(), now(), 2),
    (180.00, 'Thermal Scope', 20, 'SIGHT', true, false, 'ACCESSORY', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, now(), now(), 3),
    (500.00, 'Machine Gun Z', 3, NULL, true, false, 'WEAPON', NULL, 'HEAVY_NATO', 15.0, 130, 1000, 'MACHINEGUN', 50, 'DRUM', NULL, now(), now(), 4),
    (75.00, 'Muzzle Device', 30, 'MUZZLE_DEVICE', true, false, 'ACCESSORY', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, now(), now(), 5),
    (250.00, 'Night Vision Scope', 5, 'SIGHT', true, false, 'ACCESSORY', 6.0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, now(), now(), 6);
INSERT INTO auctions (id, owner_id, pretender_id, closed, start_price, last_price, price_step, title, description, created_at, updated_at, closing_at)
VALUES
    (1, 1, 2, false, 500.00, 550.00, 50.00, 'First Auction', 'Auction for Sniper Rifle X', now(), now(), now() + interval '10 days'),
    (2, 3, 1, true, 300.00, 450.00, 25.00, 'Second Auction', 'Auction for Red Dot Sight', now() - interval '15 days', now(), now() + interval '5 days'),
    (3, 4, 5, false, 1000.00, 1100.00, 100.00, 'Third Auction', 'Auction for Assault Rifle Y', now(), now(), now() + interval '7 days'),
    (4, 2, 6, true, 700.00, 800.00, 50.00, 'Fourth Auction', 'Auction for Thermal Scope', now() - interval '10 days', now(), now() + interval '3 days');
INSERT INTO product_orders (id, buyer_id, seller_id, price, delivered, confirmed, created_at, updated_at)
VALUES
    (1, 1, 2, 1000.00, true, true, now(), now()),
    (2, 2, 3, 500.00, false, false, now(), now()),
    (3, 4, 1, 1800.00, false, false, now(), now()),
    (4, 3, 2, 150.00, true, true, now(), now()),
    (5, 5, 6, 250.00, false, true, now(), now()),
    (6, 6, 4, 1200.00, true, false, now(), now());
INSERT INTO product_filters (user_id, product_type, caliber, accessory_type, max_price, weapon_type, name)
VALUES
    (1, 'WEAPON', 'RIFLE_NATO', NULL, 1500.00, 'SNIPER_RIFLE', 'Sniper Rifle'),
    (2, 'AMMO', 'PISTOL_NATO', NULL, 200.00, NULL, 'Ammo'),
    (3, 'ACCESSORY', NULL, 'SIGHT', 300.00, NULL, 'Red Dot Sight'),
    (4, 'WEAPON', 'HEAVY_NATO', NULL, 2500.00, 'MACHINEGUN', 'Heavy Machine Gun'),
    (5, 'ACCESSORY', NULL, 'MUZZLE_DEVICE', 100.00, NULL, 'Muzzle Devices'),
    (6, 'ACCESSORY', NULL, 'SIGHT', 300.00, NULL, 'Night Vision Scopes');
INSERT INTO user_authorities (user_id, authority)
VALUES
    (1, 'ROLE_USER'),
    (2, 'ROLE_USER'),
    (3, 'ROLE_ADMIN'),
    (4, 'ROLE_USER'),
    (5, 'ROLE_USER'),
    (6, 'ROLE_USER');
INSERT INTO user_products (user_id, product_id)
VALUES
    (1, 1),  -- User 1 owns the Sniper Rifle
    (2, 2),  -- User 2 owns the 9mm Ammo
    (3, 3),  -- User 3 owns the Red Dot Sight
    (4, 4),  -- User 4 owns the Machine Gun Z
    (5, 5),  -- User 5 owns the Muzzle Device
    (6, 6);  -- User 6 owns the Night Vision Scope
INSERT INTO user_wishlist (user_id, product_id)
VALUES
    (1, 2),  -- User 1 wishes to have the 9mm Ammo
    (2, 1),  -- User 2 wishes to have the Sniper Rifle
    (3, 2),  -- User 3 wishes to have the 9mm Ammo
    (4, 3),  -- User 4 wishes to have the Thermal Scope
    (5, 4),  -- User 5 wishes to have the Machine Gun Z
    (6, 1);  -- User 6 wishes to have the Sniper Rifle X
INSERT INTO user_wish_notified_products (user_id, product_id)
VALUES
    (1, 3),  -- User 1 was notified about the Red Dot Sight
    (2, 3),  -- User 2 was notified about the Red Dot Sight
    (3, 1),  -- User 3 was notified about the Sniper Rifle
    (4, 1),  -- User 4 was notified about the Sniper Rifle X
    (5, 3),  -- User 5 was notified about the Thermal Scope
    (6, 4);  -- User 6 was notified about the Machine Gun Z

