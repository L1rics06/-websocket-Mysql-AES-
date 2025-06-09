CREATE DATABASE IF NOT EXISTS chatdata
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;
USE chatdata;
CREATE TABLE clientinfo (
    ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    gender VARCHAR(10),
    age INT,
    password VARCHAR(255),
    email VARCHAR(255),
    avatar VARCHAR(255),
    signature VARCHAR(255)
);
CREATE TABLE history (
                         name VARCHAR(255),
                         content TEXT,
                         time DATETIME(3)
);
CREATE TABLE images(
                       image_name VARCHAR(255),
                       image_data varchar(255)
);
CREATE TABLE emojis (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        code VARCHAR(10),
                        description VARCHAR(255)
);
