-- Create the enum types for PostgreSQL

CREATE TYPE caliber AS ENUM ('AK_47', 'RIFLE_NATO', 'RIFLE_SOVIET', 'ACP', 'HEAVY_NATO', 'HEAVY_SOVIET', 'PISTOL_NATO', 'PISTOL_SOVIET', 'AR_NATO', 'AR_SOVIET');

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
    avatar bytea,  -- Аватар пользователя
    created_at TIMESTAMP,  -- Дата создания
    updated_at TIMESTAMP,  -- Дата обновления
    CONSTRAINT unique_email UNIQUE (email),  -- Уникальность email
    CONSTRAINT unique_phone_number UNIQUE (phone_number)  -- Уникальность номера телефона
);

-- Создание таблицы для ролей/полномочий
CREATE TABLE user_authorities (
    user_id BIGINT NOT NULL,              -- Внешний ключ на пользователя
    authorities VARCHAR(255) NOT NULL,      -- Роль или полномочие (например, "ROLE_USER")
    PRIMARY KEY (user_id, authorities),     -- Составной первичный ключ для user_id + authorities
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE  -- Внешний ключ на таблицу users
);

-- Create the product table
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT, --NOT NULL,  -- Внешний ключ на таблицу users
    price NUMERIC,
    name VARCHAR(255),
    qty INT,
    accessory_type SMALLINT,
    for_sale BOOLEAN,
    hidden BOOLEAN,
    image BYTEA,
    product_type SMALLINT,
    magnification NUMERIC,
    sight_type SMALLINT,
    caliber caliber,
    weight NUMERIC,
    length INT,
    rate_of_fire INT,
    weapon_type SMALLINT,
    barrel_length INT,
    feeding_type SMALLINT,
    ammo_type VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);


-- Создаем таблицу для коллекции ProductFilter, связанной с таблицей users
CREATE TABLE product_filters (
    user_id BIGINT NOT NULL,  -- Внешний ключ на таблицу users
    product_type SMALLINT,  -- Используем перечисление для типа продукта
    caliber caliber,  -- Используем перечисление для калибра
    accessory_type SMALLINT,  -- Используем перечисление для типа аксессуаров
    max_price FLOAT,
    weapon_type SMALLINT,  -- Используем перечисление для типа оружия
    name VARCHAR(255),
    PRIMARY KEY (user_id, name),  -- Уникальный фильтр на основе имени и пользователя
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
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
    FOREIGN KEY (buyer_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (seller_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Создаем таблицу для связи между заказами и продуктами
CREATE TABLE product_orders_products (
    product_order_id BIGINT NOT NULL,  -- Внешний ключ на таблицу product_orders
    products_id BIGINT NOT NULL,  -- Внешний ключ на таблицу products
    PRIMARY KEY (product_order_id, products_id),
    FOREIGN KEY (product_order_id) REFERENCES product_orders(id) ON DELETE CASCADE,
    FOREIGN KEY (products_id) REFERENCES products(id) ON DELETE CASCADE
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
    FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (pretender_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Создание таблицы для связи между аукционами и продуктами
CREATE TABLE auctions_products (
    auction_id BIGINT NOT NULL,  -- Внешний ключ на таблицу auctions
    products_id BIGINT NOT NULL,  -- Внешний ключ на таблицу products
    PRIMARY KEY (auction_id, products_id),
    FOREIGN KEY (auction_id) REFERENCES auctions(id) ON DELETE CASCADE,
    FOREIGN KEY (products_id) REFERENCES products(id) ON DELETE CASCADE
);

-- Таблица для связи пользователей и фильтров продуктов (UserProductFilters)
CREATE TABLE users_product_filters (
    user_id BIGINT NOT NULL,  -- Внешний ключ на таблицу users
    name VARCHAR(255) NOT NULL,  -- Часть Внешнего ключа на таблицу product_filters
    PRIMARY KEY (user_id, name),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id,name) REFERENCES product_filters(user_id,name) ON DELETE CASCADE
);

-- Таблица для хранения продуктов пользователя
CREATE TABLE users_products (
    user_id BIGINT NOT NULL,  -- Внешний ключ на таблицу users
    products_id BIGINT NOT NULL,  -- Внешний ключ на таблицу products
    PRIMARY KEY (user_id, products_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (products_id) REFERENCES products(id) ON DELETE CASCADE
);

-- Таблица для хранения товаров в списке желаемого пользователя
CREATE TABLE users_wish_list (
    user_id BIGINT NOT NULL,  -- Внешний ключ на таблицу users
    wish_list_id BIGINT NOT NULL,  -- Внешний ключ на таблицу products
    PRIMARY KEY (user_id, wish_list_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (wish_list_id) REFERENCES products(id) ON DELETE CASCADE
);

-- Таблица для хранения продуктов, о которых было уведомлено
CREATE TABLE users_wish_notified_products (
    user_id BIGINT NOT NULL,  -- Внешний ключ на таблицу users
    wish_notified_products_id BIGINT NOT NULL,  -- Внешний ключ на таблицу products
    PRIMARY KEY (user_id, wish_notified_products_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (wish_notified_products_id) REFERENCES products(id) ON DELETE CASCADE
);

--
INSERT INTO users (id, username, password, rating, ratings_number, email, phone_number, active, order_history_hidden, created_at, updated_at, avatar)
VALUES
    (1, 'user1', '$2a$10$X6oVYkw6syn7.CMgJt4dmuItqvDCgPcZ8YIDsyoSfz4rOOoNCDsXe', 4.5, 10, 'user1@example.com', '+123456789', true, false, now(), now(), pg_read_binary_file('D:\weaponplace_avatars\user1.jpg')),
    (2, 'user2', '$2a$10$Ir66w0XFx81UYz1HqeV3fejKZMMqUBiWxPkXv3TMfYCi2fiCiSPBa', 3.0, 20, 'user2@example.com', '+987654321', true, true, now(), now(), pg_read_binary_file('D:\weaponplace_avatars\user2.jpg')),
    (3, 'admin', '$2a$10$Zaub7EV.yacAePlUCS.G2OyVMZBpFwjIaw0zif5b9Xk7WhpTBHYKe', 5.0, 50, 'admin@example.com', '+1122334455', true, false, now(), now(), pg_read_binary_file('D:\weaponplace_avatars\admin.jpg'));

-- Вставка ролей для пользователей
INSERT INTO user_authorities (user_id, authorities)
VALUES
    (1, 'ROLE_USER'),
    (2, 'ROLE_USER'),
    (3, 'ROLE_ADMIN');

INSERT INTO products (id,price, name, qty, accessory_type, for_sale, hidden, product_type, magnification, caliber, weight, length, rate_of_fire, weapon_type, barrel_length, feeding_type, ammo_type, created_at, updated_at,user_id)
VALUES
    (1,1000.00, 'Sniper Rifle X', 5, NULL, true, false, 0, 4.0, 'RIFLE_NATO', 10.5, 120, 10, 0, 30, 1, NULL, now(), now(),1),
    (2,150.00, '9mm Ammo', 1000, NULL, true, false, 2, NULL, 'PISTOL_NATO', NULL, NULL, NULL, NULL, NULL, NULL, 'PISTOL_NATO', now(), now(),2),
    (3,250.00, 'Red Dot Sight', 50, 3, true, false, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, now(), now(),3),
    (4,2000.00, 'Assault Rifle Y', 10, NULL, true, false, 0, NULL, 'AR_NATO', 7.5, 90, 600, 3, NULL, 2, NULL, now(), now(), 1),
    (5,100.00, '5.56mm Ammo', 2000, NULL, true, false, 2, NULL, 'AR_NATO', NULL, NULL, NULL, NULL, NULL, NULL, 'AR_NATO', now(), now(), 2),
    (6,180.00, 'Thermal Scope', 20, 3, true, false, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, now(), now(), 3),
    (7,500.00, 'Machine Gun Z', 3, NULL, true, false, 0, NULL, 'HEAVY_NATO', 15.0, 130, 1000, 1, 50, 1, NULL, now(), now(), 1),
    (8,75.00, 'Muzzle Device', 30, 1, true, false, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, now(), now(), 2),
    (9,250.00, 'Night Vision Scope', 5, 3, true, false, 1, 6.0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, now(), now(), 3),
    -- Пример оружия (снайперская винтовка)
    (10, 1200.00, 'Barrett M82', 5, NULL, TRUE, FALSE, 0, NULL, 'HEAVY_NATO', 14.2, 1200, 600, 0, 700, 2, 'API', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
    -- Пример аксессуара (прицел RedDot)
    (11, 150.00, 'Aimpoint CompM4', 10, 3, TRUE, FALSE, 1, NULL, NULL, 0.5, 10, NULL, NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2),
    -- Пример патронов (7.62x51)
    (12, 200.00, '7.62x51 NATO', 50, NULL, TRUE, FALSE, 2, NULL, 'RIFLE_NATO', 0.04, 3, NULL, NULL, NULL, NULL, 'FMJ', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3),
    -- Пример оружия (автомат)
    (13, 900.00, 'AK-74M', 8, NULL, TRUE, FALSE, 0, NULL, 'AR_SOVIET', 3.3, 880, 700, 3, 415, 2, 'FMJ', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
    -- Пример аксессуара (глушитель)
    (14, 250.00, 'SilencerCo Osprey', 12, 1, TRUE, FALSE, 1, NULL, NULL, 0.9, 20, NULL, NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2),
    -- Пример патронов (9x19)
    (15, 100.00, '9mm Luger', 100, NULL, TRUE, FALSE, 2, NULL, 'PISTOL_NATO', 0.01, 2, NULL, NULL, NULL, 2, 'FMJ', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3);
INSERT INTO auctions (id, owner_id, pretender_id, closed, start_price, last_price, price_step, title, description, created_at, updated_at, closing_at)
VALUES
    (1, 1, 2, false, 500.00, 550.00, 50.00, 'First Auction', 'Auction for Sniper Rifle X', now(), now(), now() + interval '10 days'),
    (2, 3, 1, true, 300.00, 450.00, 25.00, 'Second Auction', 'Auction for Red Dot Sight', now() - interval '15 days', now(), now() + interval '5 days'),
    (3, 2, 1, false, 1000.00, 1100.00, 100.00, 'Third Auction', 'Auction for Assault Rifle Y', now(), now(), now() + interval '7 days'),
    (4, 2, 3, true, 700.00, 800.00, 50.00, 'Fourth Auction', 'Auction for Thermal Scope', now() - interval '10 days', now(), now() + interval '3 days');

INSERT INTO product_orders (id, buyer_id, seller_id, price, delivered, confirmed, created_at, updated_at)
VALUES
    (1, 1, 2, 1000.00, true, true, now(), now()),
    (2, 2, 3, 500.00, false, false, now(), now()),
    (3, 3, 1, 1800.00, false, false, now(), now()),
    (4, 3, 2, 150.00, true, true, now(), now()),
    (5, 2, 1, 250.00, false, true, now(), now()),
    (6, 1, 3, 1200.00, true, false, now(), now());
INSERT INTO product_filters (user_id, product_type, caliber, accessory_type, max_price, weapon_type, name)
VALUES
    (1, 0, 'RIFLE_NATO', NULL, 1500.00, 0, 'Sniper Rifle'),
    (2, 2, 'PISTOL_NATO', NULL, 200.00, NULL, 'Ammo'),
    (3, 1, NULL, 3, 300.00, NULL, 'Red Dot Sight'),
    (1, 0, 'HEAVY_NATO', NULL, 2500.00, 1, 'Heavy Machine Gun'),
    (2, 1, NULL, 1, 100.00, NULL, 'Muzzle Devices'),
    (3, 1, NULL, 3, 300.00, NULL, 'Night Vision Scopes');
INSERT INTO users_products (user_id, products_id)
VALUES
    (1, 1),  -- User 1 owns the Sniper Rifle
    (2, 2),  -- User 2 owns the 9mm Ammo
    (3, 3),  -- User 3 owns the Red Dot Sight
    (1, 4),  -- User 1 owns the Machine Gun Z
    (2, 5),  -- User 2 owns the Muzzle Device
    (3, 6);  -- User 3 owns the Night Vision Scope
INSERT INTO users_wish_list (user_id, wish_list_id)
VALUES
    (1, 2),  -- User 1 wishes to have the 9mm Ammo
    (2, 1),  -- User 2 wishes to have the Sniper Rifle
    (3, 2),  -- User 3 wishes to have the 9mm Ammo
    (1, 3),  -- User 1 wishes to have the Thermal Scope
    (2, 4),  -- User 2 wishes to have the Machine Gun Z
    (3, 1);  -- User 3 wishes to have the Sniper Rifle X
INSERT INTO users_wish_notified_products (user_id, wish_notified_products_id)
VALUES
    (1, 3),  -- User 1 was notified about the Red Dot Sight
    (2, 6),  -- User 2 was notified about the Red Dot Sight
    (3, 7),  -- User 3 was notified about the Sniper Rifle
    (1, 1),  -- User 1 was notified about the Sniper Rifle X
    (2, 9),  -- User 2 was notified about the Thermal Scope
    (3, 4);  -- User 3 was notified about the Machine Gun Z
INSERT INTO product_orders_products (product_order_id, products_id)
VALUES
    (1, 1),  -- Продукт 1 в заказе 1
    (2, 3),  -- Продукт 3 в заказе 2
    (3, 5),  -- Продукт 5 в заказе 3
    (4, 7),  -- Продукт 7 в заказе 4
    (5, 8),  -- Продукт 8 в заказе 5
    (6, 9);  -- Продукт 9 в заказе 6
INSERT INTO auctions_products (auction_id, products_id) VALUES
-- Продукты на аукцион 1
(1, 4),  -- AK-74M
(1, 6),  -- 7.62x51 NATO
(1, 10), -- Aimpoint CompM4
(1, 11), -- SilencerCo Osprey

-- Продукты на аукцион 2
(2, 12), -- Aimpoint CompM4
(2, 13), -- SilencerCo Osprey

-- Продукты на аукцион 3
(3, 10), -- Aimpoint CompM4
(3, 14), -- Barrett M82
(3, 15), -- 9mm Luger

-- Продукты на аукцион 4
(4, 4),  -- AK-74M
(4, 6),  -- 7.62x51 NATO
(4, 13), -- Aimpoint CompM4
(4, 14); -- Barrett M82

SELECT setval('auctions_id_seq', 5);
SELECT setval('products_id_seq', 16);
SELECT setval('product_orders_id_seq', 7);
SELECT setval('users_id_seq', 4);