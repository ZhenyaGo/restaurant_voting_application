INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);


INSERT INTO restaurants (name, address)
VALUES ('White Rabbit', 'Смоленская площадь, 3'),
       ('Selfie', 'Новинский бул., 31'),
       ('Twins Garden', 'Страстной бул., 8'),
       ('Grand Cru', 'Малая Бронная ул., 22');

INSERT INTO dishes (restaurant_id, name , price, registered)
VALUES (1, 'Pasta Carbonara', 450, '2021-12-01'),
       (1, 'Oysters', 700, '2021-12-01'),
       (1, 'Fried eggs', 200, '2021-12-01'),
       (2, 'Tiramisu', 400, '2021-12-01'),
       (2, 'Cherry pie', 450, '2021-12-01'),
       (2, 'Pancakes', 300, '2021-12-01'),
       (3, 'Seafood pasta', 450, '2021-12-01'),
       (3, 'Meatballs', 350, '2021-12-01'),
       (3, 'Pizza', 600, '2021-12-01'),
       (4, 'Risotto', 450, '2021-12-01'),
       (4, 'Steak ', 1000, '2021-12-01'),
       (4, 'Lasagna', 450, '2021-12-01'),
       (1, 'Vegetable soup', 325, current_date),
       (1, 'Greek salad', 400, current_date),
       (1, 'Wine', 350, current_date),
       (3, 'Onion rings', 344, current_date),
       (3, 'Garlic bread', 250, current_date),
       (3, 'Milkshake', 285, current_date);



INSERT INTO voting (user_id, restaurant_id, voting_date, voting_time)
VALUES (1, 2, '2021-12-05', '10:00:00'),
       (1, 4, '2021-12-06', '14:00:00'),
       (1, 3, '2021-12-07', '12:00:00');