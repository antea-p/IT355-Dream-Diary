DROP SCHEMA IF EXISTS dream_diary CASCADE;
CREATE SCHEMA dream_diary;

CREATE TABLE dream_diary.users
(
    user_id  int          NOT NULL PRIMARY KEY,
    username varchar(50)  NOT NULL UNIQUE,
    password varchar(150) NOT NULL
);

-- TODO: Insert other tables and entries, later