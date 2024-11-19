CREATE DATABASE mydatabase;
USE mydatabase;

DROP TABLE IF EXISTS bv_brands;
DROP TABLE IF EXISTS bv_categories;
DROP TABLE IF EXISTS bv_products;
DROP TABLE IF EXISTS bv_products_details;
DROP TABLE IF EXISTS bv_products_photos;
DROP TABLE IF EXISTS bv_brands_categories;
DROP TABLE IF EXISTS bv_products_categories;
DROP TABLE IF EXISTS bv_users_roles;
DROP TABLE IF EXISTS bv_roles;
DROP TABLE IF EXISTS bv_users;

CREATE TABLE bv_brands (
    id INT AUTO_INCREMENT PRIMARY KEY,
    brand_name VARCHAR(100),
    brand_ph_content MEDIUMBLOB
);

CREATE TABLE bv_categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    category_parent_id INT,
    category_name VARCHAR(100),
    category_ph_content MEDIUMBLOB,
    FOREIGN KEY (category_parent_id) REFERENCES bv_categories(id) ON DELETE SET NULL
);

CREATE TABLE bv_products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_brand INT,
    product_name VARCHAR(100),
    product_main_photo MEDIUMBLOB,
    product_short_desc VARCHAR(100),
    product_long_desc VARCHAR(500),
    product_price FLOAT,
    product_discount FLOAT,
    products_status TINYINT(1),
    products_has_stock TINYINT(1),
    product_width FLOAT,
    product_length FLOAT,
    product_height FLOAT,
    product_cost FLOAT,
    product_creation_time DATETIME,
    product_update_time DATETIME,
    FOREIGN KEY (id_brand) REFERENCES bv_brands(id) ON DELETE SET NULL
);

CREATE TABLE bv_products_details (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_product INT NOT NULL,
    detail_name VARCHAR(100),
    detail_value VARCHAR(100),
    FOREIGN KEY (id_product) REFERENCES bv_products(id) ON DELETE CASCADE
);

CREATE TABLE bv_products_photos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_product INT NOT NULL,
    products_ph_contents MEDIUMBLOB,
    FOREIGN KEY (id_product) REFERENCES bv_products(id) ON DELETE CASCADE
);

CREATE TABLE bv_products_categories (
    category_id INT NOT NULL,
    product_id INT NOT NULL,
    PRIMARY KEY (category_id, product_id),
    FOREIGN KEY (category_id) REFERENCES bv_categories(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES bv_products(id) ON DELETE CASCADE
);

CREATE TABLE bv_brands_categories (
    brand_id INT NOT NULL,
    category_id INT NOT NULL,
    PRIMARY KEY (brand_id, category_id),
    FOREIGN KEY (brand_id) REFERENCES bv_brands(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES bv_categories(id) ON DELETE CASCADE
);

CREATE TABLE bv_roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(100) UNIQUE NOT NULL,
    role_description VARCHAR(255)
);

CREATE TABLE bv_users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    user_last_name VARCHAR(100),
    user_email VARCHAR(100) UNIQUE NOT NULL,
    user_password VARCHAR(100) NOT NULL,
    user_status TINYINT(1) NOT NULL,
    user_ph_content MEDIUMBLOB
);

CREATE TABLE bv_users_roles (
    users_id INT NOT NULL,
    roles_id INT NOT NULL,
    PRIMARY KEY (users_id, roles_id),
    FOREIGN KEY (users_id) REFERENCES bv_users(id) ON DELETE CASCADE,
    FOREIGN KEY (roles_id) REFERENCES bv_roles(id) ON DELETE CASCADE
);

INSERT INTO bv_roles(role_name, role_description) 
VALUES 
    ("ADMIN", "Role Admin in Blue Velvet Music Store"),
    ("USER", "Role User in Blue Velvet Music Store"),
    ("EDITOR", "Role Editor in Blue Velvet Music Store");

INSERT INTO bv_categories(category_parent_id, category_name, category_ph_content) VALUES (1, "Categoria Pai", NULL);

INSERT INTO bv_users(username, user_last_name, user_email, user_password, user_status, user_ph_content)
VALUES ("Florentino", "Santos", "florentino@bluevelvet.com", "$2a$10$loSTN.7GhTJ5Zgdek7y38eJnw58HFwnKrn1pmx.hsD4fNvnQUD2Iu", 1, "iVBORw0KGgoAAAANSUhEUgAAAGUAAABeCAIAAAC1o032AAAACXBIWXMAAA7EAAAOxAGVKw4bAAAA3ElEQVR4nO3QwQ3AIBDAsNL9dz5WIC");

INSERT INTO bv_users_roles(users_id, roles_id) VALUES (1,1);

INSERT INTO bv_brands(brand_name, brand_ph_content) VALUES ("Brand1", null);

INSERT INTO bv_products (
    id_brand,
    product_name,
    product_main_photo,
    product_short_desc,
    product_long_desc,
    product_price,
    product_discount,
    products_status,
    products_has_stock,
    product_width,
    product_length,
    product_height,
    product_cost,
    product_creation_time,
    product_update_time
) VALUES
(1, "Produto de Teste 1", "iVBORw0KGgoAAAANSUhEUgAAAGUAAABeCAIAAAC1o032AAAACXBIWXMAAA7EAAAOxAGVKw4bAAAA3ElEQVR4nO3QwQ3AIBDAsNL9dz5WIC+EZE8QZc3Mx7H/dsBj/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8ajYNfwO5QbHnsQAAAABJRU5ErkJggg==", "Descrição curta do Produto 1", "Descrição longa detalhando o Produto de Teste 1", 150.00, 5.00, 1, 1, 10.0, 20.0, 30.0, 100.00, NOW(), NOW()),
(1, "Produto de Teste 2", "iVBORw0KGgoAAAANSUhEUgAAAGUAAABeCAIAAAC1o032AAAACXBIWXMAAA7EAAAOxAGVKw4bAAAA3ElEQVR4nO3QwQ3AIBDAsNL9dz5WIC+EZE8QZc3Mx7H/dsBj/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8ajYNfwO5QbHnsQAAAABJRU5ErkJggg==", "Descrição curta do Produto 2", "Descrição longa detalhando o Produto de Teste 2", 200.00, 10.00, 1, 1, 15.0, 25.0, 35.0, 120.00, NOW(), NOW());

INSERT INTO bv_products_details (id_product, detail_name, detail_value) 
VALUES
(1, 'Detalhe 1 Produto 1', 'Valor Detalhe 1'),
(2, 'Detalhe 1 Produto 2', 'Valor Detalhe 1');

INSERT INTO bv_products_photos (id_product, products_ph_contents) 
VALUES
(1, "iVBORw0KGgoAAAANSUhEUgAAAGUAAABeCAIAAAC1o032AAAACXBIWXMAAA7EAAAOxAGVKw4bAAAA3ElEQVR4nO3QwQ3AIBDAsNL9dz5WIC+EZE8QZc3Mx7H/dsBj/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8ajYNfwO5QbHnsQAAAABJRU5ErkJggg=="),  
(2, "iVBORw0KGgoAAAANSUhEUgAAAGUAAABeCAIAAAC1o032AAAACXBIWXMAAA7EAAAOxAGVKw4bAAAA3ElEQVR4nO3QwQ3AIBDAsNL9dz5WIC+EZE8QZc3Mx7H/dsBj/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8ajYNfwO5QbHnsQAAAABJRU5ErkJggg==");  

INSERT INTO bv_products_categories (category_id, product_id) 
VALUES
(1, 1),  
(1, 2); 




