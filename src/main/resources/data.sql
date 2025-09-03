INSERT INTO places (id, name, country, city, description, hashtag)
VALUES ('9462ed41-dbb6-4cbc-9cbf-2febe16c08d0', 'Podbara', 'Serbia', 'Novi Sad', 'A default place for users', '#default')
ON CONFLICT (id) DO NOTHING;

INSERT INTO users (id, first_name, last_name, email, password, role, place_id)
VALUES ('14d233b9-827b-41da-81a7-9fe2b191d4de', 'admin', 'admin', 'admin@gmail.com', 'admin', 'ADMIN', '9462ed41-dbb6-4cbc-9cbf-2febe16c08d0')
ON CONFLICT (id) DO NOTHING;

INSERT INTO users (id, first_name, last_name, email, password, role, place_id)
VALUES ('157e5635-5ee3-44f1-9f15-64029c54d37a', 'Davor', 'Homa', 'davor@gmail.com', 'davor', 'USER', '9462ed41-dbb6-4cbc-9cbf-2febe16c08d0')
ON CONFLICT (id) DO NOTHING;

INSERT INTO users (id, first_name, last_name, email, password, role, place_id)
VALUES ('750107a7-4ccf-4b70-a52a-b73fb36f2e64', 'Ana', 'Anic', 'ana@gmail.com', 'ana', 'USER', '9462ed41-dbb6-4cbc-9cbf-2febe16c08d0')
ON CONFLICT (id) DO NOTHING;