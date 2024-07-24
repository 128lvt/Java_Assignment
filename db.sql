create database onlineshop;

use onlineshop;

CREATE TABLE categories (
    id int IDENTITY NOT NULL,
    [name] varchar(100) NULL,
    PRIMARY KEY (id)
);

CREATE TABLE order_details (
    id int IDENTITY NOT NULL,
    number_of_product int NULL,
    price float NULL,
    product_id int NOT NULL,
    order_id int NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE orders (
    id int IDENTITY NOT NULL,
    order_date date NULL,
    total_money float NULL,
    [user_id] int NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE product_img (
    id int IDENTITY NOT NULL,
    img_url varchar(300) NULL,
    product_id int NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE products (
    id int IDENTITY NOT NULL,
    [name] varchar(350) NULL,
    price float NULL,
    [description] varchar(300) NULL,
    created_at date NULL,
    updated_at date NULL,
    category_id int NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE users (
    id int IDENTITY NOT NULL,
    fullname varchar(100) NULL,
    phone_number varchar(20) NULL,
    [address] varchar(100) NULL,
    [password] varchar(100) NULL,
    created_at date NULL,
    updated_at date NULL,
    is_active bit NULL,
    username varchar(100) NULL,
    is_admin bit NULL,
    PRIMARY KEY (id)
);

ALTER TABLE
    products
ADD
    CONSTRAINT FKproducts963487 FOREIGN KEY (category_id) REFERENCES Categories (id);

ALTER TABLE
    orders
ADD
    CONSTRAINT FKorders322301 FOREIGN KEY ([user_id]) REFERENCES users (id);

ALTER TABLE
    order_details
ADD
    CONSTRAINT FKorder_deta881673 FOREIGN KEY (product_id) REFERENCES products (id);

ALTER TABLE
    order_details
ADD
    CONSTRAINT FKorder_deta168320 FOREIGN KEY (order_id) REFERENCES orders (id);

ALTER TABLE
    product_img
ADD
    CONSTRAINT FKproduct_im53257 FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE;

INSERT INTO
    Categories (name)
VALUES
    ('Mens Clothing'),
    ('Womens Clothing'),
    ('Kids Clothing'),
    ('Accessories');

INSERT INTO
    products (
        [name],
        price,
        [description],
        created_at,
        updated_at,
        category_id
    )
VALUES
    (
        'Men T-Shirt',
        19.99,
        'A comfortable cotton t-shirt for men',
        '2023-01-15',
        NULL,
        1
    ),
    (
        'Women T-Shirt',
        21.99,
        'A stylish t-shirt for women',
        '2023-01-16',
        NULL,
        2
    ),
    (
        'Kid Jacket',
        35.50,
        'A warm jacket for kids',
        '2023-01-17',
        NULL,
        3
    ),
    (
        'Men Jeans',
        45.00,
        'Denim jeans for men',
        '2023-01-18',
        NULL,
        1
    ),
    (
        'Women Jeans',
        47.00,
        'Fashionable jeans for women',
        '2023-01-19',
        NULL,
        2
    ),
    (
        'Kid Trousers',
        22.00,
        'Comfortable trousers for kids',
        '2023-01-20',
        NULL,
        3
    ),
    (
        'Hat',
        15.00,
        'A stylish hat',
        '2023-01-21',
        NULL,
        4
    ),
    (
        'Sunglasses',
        25.00,
        'UV-protected sunglasses',
        '2023-01-22',
        NULL,
        4
    ),
    (
        'Belt',
        18.00,
        'Leather belt',
        '2023-01-23',
        NULL,
        4
    ),
    (
        'Scarf',
        12.00,
        'Warm scarf',
        '2023-01-24',
        NULL,
        4
    );

INSERT INTO
    users (
        fullname,
        phone_number,
        [address],
        [password],
        created_at,
        updated_at,
        is_active,
        username,
        is_admin
    )
VALUES
    (
        'John Doe',
        '123-456-7890',
        '123 Main St',
        'password123',
        '2023-01-10',
        NULL,
        1,
        'johndoe',
        0
    ),
    (
        'Jane Smith',
        '234-567-8901',
        '456 Oak St',
        'password456',
        '2023-01-11',
        NULL,
        1,
        'janesmith',
        1
    ),
    (
        'Alice Johnson',
        '345-678-9012',
        '789 Pine St',
        'password789',
        '2023-01-12',
        NULL,
        1,
        'alicejohnson',
        0
    ),
    (
        'Bob Brown',
        '456-789-0123',
        '321 Maple St',
        'password012',
        '2023-01-13',
        NULL,
        1,
        'bobbrown',
        1
    ),
    (
        'Charlie Black',
        '567-890-1234',
        '654 Elm St',
        'password345',
        '2023-01-14',
        NULL,
        0,
        'charlieblack',
        0
    ),
    (
        'Diana Green',
        '678-901-2345',
        '987 Cedar St',
        'password678',
        '2023-01-15',
        NULL,
        1,
        'dianagreen',
        0
    ),
    (
        'Eve White',
        '789-012-3456',
        '147 Spruce St',
        'password901',
        '2023-01-16',
        NULL,
        1,
        'evewhite',
        1
    ),
    (
        'Frank Silver',
        '890-123-4567',
        '258 Birch St',
        'password234',
        '2023-01-17',
        NULL,
        0,
        'franksilver',
        0
    );

-- Explanation of 'is_admin':
-- 0 - Not an admin
-- 1 - Admin
INSERT INTO
    product_img (img_url, product_id)
VALUES
    ('https://example.com/images/men_tshirt.jpg', 1),
    ('https://example.com/images/women_tshirt.jpg', 2),
    ('https://example.com/images/kid_jacket.jpg', 3),
    ('https://example.com/images/men_jeans.jpg', 4),
    ('https://example.com/images/women_jeans.jpg', 5),
    ('https://example.com/images/kid_trousers.jpg', 6),
    ('https://example.com/images/hat.jpg', 7),
    ('https://example.com/images/sunglasses.jpg', 8),
    ('https://example.com/images/belt.jpg', 9),
    ('https://example.com/images/scarf.jpg', 10);