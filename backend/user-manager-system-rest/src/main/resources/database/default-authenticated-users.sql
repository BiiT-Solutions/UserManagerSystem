INSERT IGNORE INTO users (id, username, email, name, lastname, password, phone, account_locked, account_blocked, account_expired, enabled) VALUES (-1, 'admin', 'admin@test.com', 'Admin', 'Admin', '$2a$12$DBg1jcOZcrONaP.aRnihBuGqfc6qI9Uu5kf7GEMT7T3qWsWPMf1BC', '+34722177548', 0, 0, 0, 1);
INSERT IGNORE INTO roles (id, name) VALUES (-2, 'admin');
INSERT IGNORE INTO roles (id, name) VALUES (-3, 'viewer');
INSERT IGNORE INTO organizations (id, name) VALUES (-4, 'localhost');
INSERT IGNORE INTO user_roles (id, organization, `user`, role) VALUES (-5, -4 , -2, -1);
INSERT IGNORE INTO user_roles (id, organization, `user`, role) VALUES (-5, -4 , -3, -1);
