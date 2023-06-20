-- User Manager System
INSERT INTO users (id, username, email, name, lastname, password, phone, account_locked, account_blocked, account_expired, enabled) VALUES (-1, 'admin@test.com', 'admin@test.com', 'Admin', 'Admin', '$2a$12$wU5OFo.IvHq7xfoHtCqp0.yzoENB.iunHuFJD6ktM9ygm3Zxbx9fq', '+34722177548', 0, 0, 0, 1);
INSERT INTO applications (id, name) VALUES (-5, 'UserManagerSystem');
INSERT INTO roles (id, name, description) VALUES (-2, 'usermanagersystem_admin', '');
INSERT INTO roles (id, name, description) VALUES (-3, 'usermanagersystem_viewer', '');
INSERT INTO groups (id, name) VALUES (-4, 'localhost');
INSERT INTO user_roles (id, user_group, role, user_role, application) VALUES (-6, -4 , -2, -1, -5);
INSERT INTO user_roles (id, user_group, role, user_role, application) VALUES (-7, -4 , -3, -1, -5);
INSERT INTO user_roles (id, user_group, role, user_role, application) VALUES (-8, null, -2, -1, null);
INSERT INTO user_roles (id, user_group, role, user_role, application) VALUES (-9, null, -3, -1, null);


-- KafkaProxy
INSERT INTO applications (id, name) VALUES (-20, 'KafkaProxy');
INSERT INTO roles (id, name, description) VALUES (-21, 'kafkaproxy_viewer', '');
INSERT INTO roles (id, name, description) VALUES (-22, 'kafkaproxy_editor', '');
INSERT INTO roles (id, name, description) VALUES (-23, 'kafkaproxy_admin', '');
INSERT INTO user_roles (id, user_group, role, user_role, application) VALUES (-24, -4 , -21, -1, -20);
INSERT INTO user_roles (id, user_group, role, user_role, application) VALUES (-25, -4 , -22, -1, -20);
INSERT INTO user_roles (id, user_group, role, user_role, application) VALUES (-26, -4 , -23, -1, -20);


-- Task Junction
INSERT INTO applications (id, name) VALUES (-30, 'TaskJunction');
INSERT INTO roles (id, name, description) VALUES (-31, 'taskjunction_viewer', '');
INSERT INTO roles (id, name, description) VALUES (-32, 'taskjunction_editor', '');
INSERT INTO roles (id, name, description) VALUES (-33, 'taskjunction_admin', '');
INSERT INTO user_roles (id, user_group, role, user_role, application) VALUES (-34, -4 , -31, -1, -30);
INSERT INTO user_roles (id, user_group, role, user_role, application) VALUES (-35, -4 , -32, -1, -30);
INSERT INTO user_roles (id, user_group, role, user_role, application) VALUES (-36, -4 , -33, -1, -30);


-- Appointment Center
INSERT INTO applications (id, name) VALUES (-40, 'AppointmentCenter');
INSERT INTO roles (id, name, description) VALUES (-41, 'appointmentcenter_viewer', '');
INSERT INTO roles (id, name, description) VALUES (-42, 'appointmentcenter_editor', '');
INSERT INTO roles (id, name, description) VALUES (-43, 'appointmentcenter_admin', '');
INSERT INTO user_roles (id, user_group, role, user_role, application) VALUES (-44, -4 , -41, -1, -40);
INSERT INTO user_roles (id, user_group, role, user_role, application) VALUES (-45, -4 , -42, -1, -40);
INSERT INTO user_roles (id, user_group, role, user_role, application) VALUES (-46, -4 , -43, -1, -40);


-- Fact Manager
INSERT INTO applications (id, name) VALUES (-50, 'FactManager');
INSERT INTO roles (id, name, description) VALUES (-51, 'factmanager_viewer', '');
INSERT INTO roles (id, name, description) VALUES (-52, 'factmanager_editor', '');
INSERT INTO roles (id, name, description) VALUES (-53, 'factmanager_admin', '');
INSERT INTO user_roles (id, user_group, role, user_role, application) VALUES (-54, -4 , -51, -1, -50);
INSERT INTO user_roles (id, user_group, role, user_role, application) VALUES (-55, -4 , -52, -1, -50);
INSERT INTO user_roles (id, user_group, role, user_role, application) VALUES (-56, -4 , -53, -1, -50);

