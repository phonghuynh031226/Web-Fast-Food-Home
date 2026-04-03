CREATE DATABASE Fast_Food_Home;
GO

USE Fast_Food_Home;
GO

CREATE TABLE Users (
    user_id INT IDENTITY(1,1) PRIMARY KEY,
    full_name NVARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NULL,
    phone VARCHAR(20) UNIQUE NULL,
    address NVARCHAR(255) NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
        CHECK (role IN ('admin', 'customer'))
);

CREATE TABLE Menus (
    menu_id INT IDENTITY(1,1) PRIMARY KEY,
    menu_name NVARCHAR(100) NOT NULL
);

CREATE TABLE Menu_Items (
    item_id INT IDENTITY(1,1) PRIMARY KEY,
    menu_id INT NOT NULL,
    item_name NVARCHAR(150) NOT NULL,
    description NVARCHAR(255) NULL,
    image VARCHAR(255) NULL,
    price DECIMAL(10,2) NOT NULL,
    status BIT DEFAULT 1,

    CONSTRAINT fk_menu_items_menu
        FOREIGN KEY (menu_id)
        REFERENCES Menus(menu_id)
);

CREATE TABLE Cart (
    user_id INT NOT NULL,
    item_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),

    PRIMARY KEY (user_id, item_id),

    CONSTRAINT fk_cart_user
        FOREIGN KEY (user_id) REFERENCES Users(user_id),

    CONSTRAINT fk_cart_item
        FOREIGN KEY (item_id) REFERENCES Menu_Items(item_id)
);

CREATE TABLE Orders (
    order_id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    customer_name NVARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address NVARCHAR(255) NOT NULL,
    note NVARCHAR(255) NULL,
    payment_method VARCHAR(20) NOT NULL
        CHECK (payment_method IN ('CASH', 'ATM')),
    total_amount DECIMAL(10,2) NOT NULL,
    created_at DATETIME DEFAULT GETDATE(),

    CONSTRAINT fk_orders_user
        FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

CREATE TABLE Order_Items (
    order_item_id INT IDENTITY(1,1) PRIMARY KEY,
    order_id INT NOT NULL,
    item_id INT NOT NULL,
    item_name NVARCHAR(150) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),

    CONSTRAINT fk_order_items_order
        FOREIGN KEY (order_id) REFERENCES Orders(order_id),

    CONSTRAINT fk_order_items_item
        FOREIGN KEY (item_id) REFERENCES Menu_Items(item_id)
);

INSERT INTO Users (full_name, email, phone, address, password, role) VALUES
(N'Nguyễn Văn A', 'a@gmail.com', '0900000001', N'HCM', '123456', 'customer'),
(N'Trần Thị B', 'b@gmail.com', '0900000002', N'Hà Nội', '123456', 'customer'),
(N'Admin', 'admin@gmail.com', '0900000003', N'HCM', 'admin123', 'admin');

INSERT INTO Menus (menu_name) VALUES
(N'Đồ ăn'),
(N'Nước uống'),
(N'Tráng miệng');

INSERT INTO Menu_Items (menu_id, item_name, description, image, price, status) VALUES
(1, N'Burger bò', N'Ngon', 'burger.jpg', 50000, 1),
(2, N'Trà sữa', N'Ngọt', 'trasua.jpg', 30000, 1),
(3, N'Bánh flan', N'Mềm', 'flan.jpg', 20000, 1);

INSERT INTO Cart (user_id, item_id, quantity) VALUES
(1, 1, 2),
(1, 2, 1),
(2, 3, 3);

INSERT INTO Orders (user_id, customer_name, phone, address, note, payment_method, total_amount) VALUES
(1, N'Nguyễn Văn A', '0900000001', N'HCM', N'Giao nhanh', 'CASH', 130000),
(2, N'Trần Thị B', '0900000002', N'Hà Nội', NULL, 'ATM', 60000),
(1, N'Nguyễn Văn A', '0900000001', N'HCM', NULL, 'CASH', 20000);

INSERT INTO Order_Items (order_id, item_id, item_name, price, quantity) VALUES
(1, 1, N'Burger bò', 50000, 2),
(1, 2, N'Trà sữa', 30000, 1),
(2, 3, N'Bánh flan', 20000, 3);