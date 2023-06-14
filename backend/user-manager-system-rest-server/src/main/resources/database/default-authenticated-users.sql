INSERT INTO users (id, username, email, name, lastname, password, phone, account_locked, account_blocked, account_expired, enabled) VALUES (-1, 'admin@test.com', 'admin@test.com', 'Admin', 'Admin', '$2a$12$wU5OFo.IvHq7xfoHtCqp0.yzoENB.iunHuFJD6ktM9ygm3Zxbx9fq', '+34722177548', 0, 0, 0, 1);
INSERT INTO applications (id, name) VALUES (-5, 'UserManagerSystem');
INSERT INTO roles (id, name, description) VALUES (-2, 'usermanagersystem_admin', '');
INSERT INTO roles (id, name, description) VALUES (-3, 'usermanagersystem_viewer', '');
INSERT INTO groups (id, name) VALUES (-4, 'localhost');
INSERT INTO user_roles (id, user_group, role, user_role, application) VALUES (-6, -4 , -2, -1, -5);
INSERT INTO user_roles (id, user_group, role, user_role, application) VALUES (-7, -4 , -3, -1, -5);
INSERT INTO user_roles (id, user_group, role, user_role, application) VALUES (-8, null, -2, -1, null);
INSERT INTO user_roles (id, user_group, role, user_role, application) VALUES (-9, null, -3, -1, null);
