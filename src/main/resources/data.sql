DROP SCHEMA IF EXISTS dream_diary CASCADE;
CREATE SCHEMA dream_diary;

CREATE TABLE dream_diary.users
(
    user_id  int          NOT NULL PRIMARY KEY,
    username varchar(50)  NOT NULL UNIQUE,
    password varchar(150) NOT NULL,
    is_admin boolean NOT NULL
);

-- TODO: Insert other tables and entries, later

INSERT INTO dream_diary.users (user_id, username, password, is_admin)
VALUES (1, 'omori', '$2a$12$hJqGL7dZIgEecr8lamjVkOM6fKSvUFWAup8KZhHD0X5ppIzNcsqXK', false),
       (2, 'sunny', '$2a$12$hJqGL7dZIgEecr8lamjVkOM6fKSvUFWAup8KZhHD0X5ppIzNcsqXK', true)
