INSERT IGNORE INTO users (id, username, email, name, lastname, password, phone, account_locked, account_blocked, account_expired, enabled) VALUES (-1, 'admin', 'admin@test.com', 'Admin', 'Admin', '$2a$12$wU5OFo.IvHq7xfoHtCqp0.yzoENB.iunHuFJD6ktM9ygm3Zxbx9fq', '+34722177548', 0, 0, 0, 1);
INSERT IGNORE INTO applications (id, name) VALUES (-5, 'UserManagerSystem');
INSERT IGNORE INTO roles (id, name, description, application) VALUES (-2, 'usermanagersystem_admin', '', -5);
INSERT IGNORE INTO roles (id, name, description, application) VALUES (-3, 'usermanagersystem_viewer', '', -5);
INSERT IGNORE INTO organizations (id, name) VALUES (-4, 'localhost');
INSERT IGNORE INTO user_roles (id, organization, role, `user`, `application`) VALUES (-6, -4 , -2, -1, -5);
INSERT IGNORE INTO user_roles (id, organization, role, `user`, `application`) VALUES (-7, -4 , -3, -1, -5);
INSERT IGNORE INTO user_roles (id, organization, role, `user`, `application`) VALUES (-8, null, -2, -1, null);
INSERT IGNORE INTO user_roles (id, organization, role, `user`, `application`) VALUES (-9, null, -3, -1, null);
