DROP SCHEMA IF EXISTS dream_diary CASCADE;
CREATE SCHEMA dream_diary;

CREATE TABLE dream_diary.users
(
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(150) NOT NULL,
    is_admin BOOLEAN NOT NULL
);

INSERT INTO dream_diary.users (user_id, username, password, is_admin)
VALUES (1, 'omori', '$2a$12$hJqGL7dZIgEecr8lamjVkOM6fKSvUFWAup8KZhHD0X5ppIzNcsqXK', false),
       (2, 'sunny', '$2a$12$hJqGL7dZIgEecr8lamjVkOM6fKSvUFWAup8KZhHD0X5ppIzNcsqXK', true);

CREATE TABLE dream_diary.diary_entries (
    entry_id SERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    created_date DATE NOT NULL,
    user_id INTEGER REFERENCES dream_diary.users(user_id) ON DELETE CASCADE
);

INSERT INTO dream_diary.diary_entries (title, content, user_id, created_date)
VALUES
('Welcome to White Space', 'I have been here as long as I can remember...', 1, '2024-05-01'),
('Exploring Neighbor''s Room', 'She has a wide array of books and colorful knick-knacks...', 1, '2024-05-02'),
('Meeting Mari', 'We gathered for a picnic with Mari, always so bright and cheerful...', 1, '2024-05-03');

CREATE TABLE dream_diary.tags (
    tag_id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    entry_id INTEGER REFERENCES dream_diary.diary_entries(entry_id) ON DELETE CASCADE
);

INSERT INTO dream_diary.tags (name, entry_id)
VALUES
('White Space', 1),
('Neighbor''s Room', 2),
('Picnic', 3);

CREATE TABLE dream_diary.emotions (
    emotion_id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

INSERT INTO dream_diary.emotions (name)
VALUES
('Nostalgia'),
('Wonder'),
('Joy');

CREATE TABLE dream_diary.diary_entry_emotions (
    entry_emotion_id SERIAL PRIMARY KEY,
    entry_id INTEGER REFERENCES dream_diary.diary_entries(entry_id) ON DELETE CASCADE,
    emotion_id INTEGER REFERENCES dream_diary.emotions(emotion_id) ON DELETE CASCADE,
    UNIQUE (entry_id, emotion_id)
);

INSERT INTO dream_diary.diary_entry_emotions (entry_id, emotion_id)
VALUES
(1, 1),
(2, 2),
(3, 3);


SELECT SETVAL('dream_diary.users_user_id_seq', (SELECT MAX(user_id) FROM dream_diary.users) + 1);
-- TODO: more sequencing resets as needed