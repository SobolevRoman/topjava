DELETE FROM user_roles;
DELETE FROM meals;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals(user_id, date_time, description, calories)
VALUES (100000, TIMESTAMP '2022-01-30 10:00:00', 'Завтрак', 400),
       (100000, TIMESTAMP '2022-01-30 13:00:00', 'Пельмени', 1000),
       (100000, TIMESTAMP '2022-01-30 20:00:00', 'Салатик', 500),
       (100000, TIMESTAMP '2022-01-31 00:00:00', 'Сало', 200),
       (100000, TIMESTAMP '2022-01-31 19:30:00', 'Ужин', 1000),
       (100000, TIMESTAMP '2022-01-31 13:25:00', 'Обед', 500),
       (100000, TIMESTAMP '2022-01-31 20:00:00', 'Ужин 2', 410),
       (100000, TIMESTAMP '2022-02-01 20:00:00', 'Ужин 010222', 510),
       (100000, TIMESTAMP '2022-02-01 10:25:00', 'Завтрак user', 650),
       (100001, TIMESTAMP '2022-01-30 10:00:00', 'Завтрак admin', 500),
       (100001, TIMESTAMP '2022-01-30 13:00:00', 'Обед', 1000),
       (100001, TIMESTAMP '2022-01-30 20:00:00', 'Ужин', 500),
       (100001, TIMESTAMP '2022-01-31 00:00:00', 'Еда', 100),
       (100001, TIMESTAMP '2022-01-31 10:00:00', 'Завтрак', 1000),
       (100001, TIMESTAMP '2022-01-31 20:00:00', 'Ужин', 410);
