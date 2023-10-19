-- User Manager System
INSERT INTO users (id, user_uuid, username, email, name, lastname, password, phone, account_locked, account_blocked, account_expired) VALUES (-1, 'e58ed763-928c-4155-bee9-fdbaaadc15f3', 'admin@test.com', 'admin@test.com', 'Admin', 'Admin', '$2a$10$V84FYKhHogkULl5OlAzw1.mLquRv0Y5kAOtYgUBnv0NuE56KGlBnO', '+34722177548', 0, 0, 0);
INSERT INTO applications (id, name) VALUES (-5, 'UserManagerSystem');
INSERT INTO roles (id, name, description) VALUES (-2, 'usermanagersystem_admin', '');
INSERT INTO roles (id, name, description) VALUES (-3, 'usermanagersystem_viewer', '');
INSERT INTO groups (id, name, application_id) VALUES (-4, 'users', -5);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-6, -4 , -2, -1);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-7, -4 , -3, -1);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-8, null, -2, -1);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-9, null, -3, -1);


-- KafkaProxy
INSERT INTO applications (id, name) VALUES (-20, 'KafkaProxy');
INSERT INTO roles (id, name, description) VALUES (-21, 'kafkaproxy_viewer', '');
INSERT INTO roles (id, name, description) VALUES (-22, 'kafkaproxy_editor', '');
INSERT INTO roles (id, name, description) VALUES (-23, 'kafkaproxy_admin', '');
INSERT INTO groups (id, name, application_id) VALUES (-24, 'users', -20);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-25, -24 , -21, -1);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-26, -24 , -22, -1);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-27, -24 , -23, -1);



-- Task Junction
INSERT INTO applications (id, name) VALUES (-30, 'TaskJunction');
INSERT INTO roles (id, name, description) VALUES (-31, 'taskjunction_viewer', '');
INSERT INTO roles (id, name, description) VALUES (-32, 'taskjunction_editor', '');
INSERT INTO roles (id, name, description) VALUES (-33, 'taskjunction_admin', '');
INSERT INTO groups (id, name, application_id) VALUES (-34, 'users', -30);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-35, -34 , -31, -1);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-36, -34 , -32, -1);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-37, -34 , -33, -1);


-- Appointment Center
INSERT INTO applications (id, name) VALUES (-40, 'AppointmentCenter');
INSERT INTO roles (id, name, description) VALUES (-41, 'appointmentcenter_viewer', '');
INSERT INTO roles (id, name, description) VALUES (-42, 'appointmentcenter_editor', '');
INSERT INTO roles (id, name, description) VALUES (-43, 'appointmentcenter_admin', '');
INSERT INTO groups (id, name, application_id) VALUES (-44, 'users', -40);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-44, -44 , -41, -1);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-45, -44 , -42, -1);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-46, -44 , -43, -1);


-- Fact Manager
INSERT INTO applications (id, name) VALUES (-50, 'FactManager');
INSERT INTO roles (id, name, description) VALUES (-51, 'factmanager_viewer', '');
INSERT INTO roles (id, name, description) VALUES (-52, 'factmanager_editor', '');
INSERT INTO roles (id, name, description) VALUES (-53, 'factmanager_admin', '');
INSERT INTO groups (id, name, application_id) VALUES (-54, 'users', -50);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-55, -54 , -51, -1);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-56, -54 , -52, -1);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-57, -54 , -53, -1);


-- Base Form Drools Engine
INSERT INTO applications (id, name) VALUES (-60, 'BaseFormDroolsEngine');
INSERT INTO roles (id, name, description) VALUES (-61, 'baseformdroolsengine_viewer', '');
INSERT INTO roles (id, name, description) VALUES (-62, 'baseformdroolsengine_editor', '');
INSERT INTO roles (id, name, description) VALUES (-63, 'baseformdroolsengine_admin', '');
INSERT INTO groups (id, name, application_id) VALUES (-64, 'users', -60);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-65, -64 , -61, -1);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-66, -64 , -62, -1);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-67, -64 , -63, -1);


-- InfographicEngine
INSERT INTO applications (id, name) VALUES (-70, 'InfographicEngine');
INSERT INTO roles (id, name, description) VALUES (-71, 'infographicengine_viewer', '');
INSERT INTO roles (id, name, description) VALUES (-72, 'infographicengine_editor', '');
INSERT INTO roles (id, name, description) VALUES (-73, 'infographicengine_admin', '');
INSERT INTO groups (id, name, application_id) VALUES (-74, 'users', -70);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-75, -74 , -71, -1);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-76, -74 , -72, -1);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-77, -74 , -73, -1);

-- Knowledge System
INSERT INTO applications (id, name) VALUES (-80, 'KnowledgeSystem');
INSERT INTO roles (id, name, description) VALUES (-81, 'knowledgesystem_viewer', '');
INSERT INTO roles (id, name, description) VALUES (-82, 'knowledgesystem_editor', '');
INSERT INTO roles (id, name, description) VALUES (-83, 'knowledgesystem_admin', '');
INSERT INTO groups (id, name, application_id) VALUES (-84, 'users', -80);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-85, -84 , -81, -1);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-86, -84 , -82, -1);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-87, -84 , -83, -1);

-- Facts Dashboard
INSERT INTO applications (id, name) VALUES (-90, 'FactsDashboard');
INSERT INTO roles (id, name, description) VALUES (-91, 'factsdashboard_employee', '');
INSERT INTO roles (id, name, description) VALUES (-92, 'factsdashboard_teamleader', '');
INSERT INTO roles (id, name, description) VALUES (-93, 'factsdashboard_ceo', '');
INSERT INTO groups (id, name, application_id) VALUES (-94, 'users', -90);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-95, -94 , -91, -1);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-96, -94 , -92, -1);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-97, -94 , -93, -1);


-- Demo Users
INSERT INTO users (id, user_uuid, username, email, name, lastname, password, phone, account_locked, account_blocked, account_expired) VALUES (-150, '15fca0dd-dd89-41dd-9448-6deb4211c204', 'customer1@test.com', 'customer1@test.com', 'Tom', 'Smith', '$2a$10$V84FYKhHogkULl5OlAzw1.mLquRv0Y5kAOtYgUBnv0NuE56KGlBnO', '+345551234', 0, 0, 0);
INSERT INTO users (id, user_uuid, username, email, name, lastname, password, phone, account_locked, account_blocked, account_expired) VALUES (-151, 'aa727351-210b-43f7-bc16-6d023ddf31b5', 'customer2@test.com', 'customer2@test.com', 'Dick', 'Taylor', '$2a$10$V84FYKhHogkULl5OlAzw1.mLquRv0Y5kAOtYgUBnv0NuE56KGlBnO', '+345552234', 0, 0, 0);
INSERT INTO users (id, user_uuid, username, email, name, lastname, password, phone, account_locked, account_blocked, account_expired) VALUES (-152, '87a580f7-4449-4b97-baee-c9f27b65ce4b', 'customer3@test.com', 'customer3@test.com', 'Harry', 'Evans', '$2a$10$V84FYKhHogkULl5OlAzw1.mLquRv0Y5kAOtYgUBnv0NuE56KGlBnO', '+345553234', 0, 0, 0);
INSERT INTO users (id, user_uuid, username, email, name, lastname, password, phone, account_locked, account_blocked, account_expired) VALUES (-153, 'd9166f80-28af-4c2b-a34a-e5b3ee37f5aa', 'customer4@test.com', 'customer4@test.com', 'Tizio', 'Russo', '$2a$10$V84FYKhHogkULl5OlAzw1.mLquRv0Y5kAOtYgUBnv0NuE56KGlBnO', '+345554234', 0, 0, 0);
INSERT INTO users (id, user_uuid, username, email, name, lastname, password, phone, account_locked, account_blocked, account_expired) VALUES (-154, '46cba6a8-f2ab-4d18-a6c3-5f1a1bcae731', 'customer5@test.com', 'customer5@test.com', 'Caio', 'Fabbro', '$2a$10$V84FYKhHogkULl5OlAzw1.mLquRv0Y5kAOtYgUBnv0NuE56KGlBnO', '+345555234', 0, 0, 0);
INSERT INTO users (id, user_uuid, username, email, name, lastname, password, phone, account_locked, account_blocked, account_expired) VALUES (-155, '47d251cf-4238-4582-b6cc-18062eacc2e8', 'customer6@test.com', 'customer6@test.com', 'Sempronio', 'Colombo', '$2a$10$V84FYKhHogkULl5OlAzw1.mLquRv0Y5kAOtYgUBnv0NuE56KGlBnO', '+345556234', 0, 0, 0);
-- Kafka Proxy Permissions
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-170, -24 , -22, -150);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-171, -24 , -22, -151);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-172, -24 , -22, -152);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-173, -24 , -22, -153);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-174, -24 , -22, -154);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-175, -24 , -22, -155);
-- Fact Manager Permissions
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-190, -54 , -51, -150);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-191, -54 , -51, -151);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-192, -54 , -51, -152);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-193, -54 , -51, -153);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-194, -54 , -51, -154);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-195, -54 , -51, -155);
-- Facts Dashboard
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-200, -94 , -91, -150);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-201, -94 , -91, -151);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-202, -94 , -91, -152);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-203, -94 , -91, -153);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-204, -94 , -91, -154);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-205, -94 , -91, -155);
-- Infographic Engine
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-210, -74 , -71, -150);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-211, -74 , -71, -151);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-212, -74 , -71, -152);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-213, -74 , -71, -153);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-214, -74 , -71, -154);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-215, -74 , -71, -155);

-- DEMO
INSERT INTO users (id, user_uuid, username, email, name, lastname, password, phone, account_locked, account_blocked, account_expired) VALUES (-301, '00976408-5ad8-48c4-a787-009cdaedf666', 'saurabh.laddha@maistering.com', 'saurabh.laddha@maistering.com', 'Saurabh', 'Laddha', '$2a$12$0i7PVFNJuYx8JeFSB8BPf.1U1O6t9C22Dxx.UMch6Q37aNrrmKnAy', '+3455512345', 0, 0, 0);
INSERT INTO users (id, user_uuid, username, email, name, lastname, password, phone, account_locked, account_blocked, account_expired) VALUES (-302, '7e629f88-9a84-404f-8148-8f85b1ce02d0', 'subir.sarbabidya@maistering.com', 'subir.sarbabidya@maistering.com', 'Subir', 'Sarbabidya', '$2a$12$0i7PVFNJuYx8JeFSB8BPf.1U1O6t9C22Dxx.UMch6Q37aNrrmKnAy', '+3455512346', 0, 0, 0);
INSERT INTO users (id, user_uuid, username, email, name, lastname, password, phone, account_locked, account_blocked, account_expired) VALUES (-303, '79b3bc0b-123a-41f7-b4fc-f669fead4292', 'prathap.chandran@maistering.com', 'prathap.chandran@maistering.com', 'Prathap', 'Chandran', '$2a$12$0i7PVFNJuYx8JeFSB8BPf.1U1O6t9C22Dxx.UMch6Q37aNrrmKnAy', '+3455512347', 0, 0, 0);
INSERT INTO users (id, user_uuid, username, email, name, lastname, password, phone, account_locked, account_blocked, account_expired) VALUES (-304, 'd854b342-028e-43b9-bdbe-31ee2a950846', 'krzysztof.sadowski@maistering.com', 'krzysztof.sadowski@maistering.com', 'Krzysztof', 'Sadowski', '$2a$12$0i7PVFNJuYx8JeFSB8BPf.1U1O6t9C22Dxx.UMch6Q37aNrrmKnAy', '+3455512348', 0, 0, 0);
INSERT INTO users (id, user_uuid, username, email, name, lastname, password, phone, account_locked, account_blocked, account_expired) VALUES (-305, '7c67fd66-3a9e-49e1-ab07-b3d2c54fe50a', 'kim.jacobs@maistering.com', 'kim.jacobs@maistering.com', 'Kim', 'Jacobs', '$2a$12$0i7PVFNJuYx8JeFSB8BPf.1U1O6t9C22Dxx.UMch6Q37aNrrmKnAy', '+3455512349', 0, 0, 0);
INSERT INTO users (id, user_uuid, username, email, name, lastname, password, phone, account_locked, account_blocked, account_expired) VALUES (-306, 'abf8e8ec-9074-4fab-bd0b-8f2ee850f44f', 'christian.clinciu@maistering.com', 'christian.clinciu@maistering.com', 'Christian', 'Clinciu', '$2a$12$0i7PVFNJuYx8JeFSB8BPf.1U1O6t9C22Dxx.UMch6Q37aNrrmKnAy', '+3455512341', 0, 0, 0);
INSERT INTO users (id, user_uuid, username, email, name, lastname, password, phone, account_locked, account_blocked, account_expired) VALUES (-307, '0414d7b8-521a-4c61-bc25-ac65f3a27797', 'hvandoorn@biit-solutions.com', 'hvandoorn@biit-solutions.com', 'Henny', 'van Doom', '$2a$12$0i7PVFNJuYx8JeFSB8BPf.1U1O6t9C22Dxx.UMch6Q37aNrrmKnAy', '+3455512342', 0, 0, 0);
INSERT INTO users (id, user_uuid, username, email, name, lastname, password, phone, account_locked, account_blocked, account_expired) VALUES (-308, 'd7e85fc3-014c-4719-a5dc-3297d71ab792', 'jorge.hortelano@maistering.com', 'jorge.hortelano@maistering.com', 'Jorge', 'Hortelano', '$2a$12$0i7PVFNJuYx8JeFSB8BPf.1U1O6t9C22Dxx.UMch6Q37aNrrmKnAy', '+3455512343', 0, 0, 0);
INSERT INTO users (id, user_uuid, username, email, name, lastname, password, phone, account_locked, account_blocked, account_expired) VALUES (-309, 'aacacce6-1092-42cc-a955-047ede1f6653', 'john.brahim@maistering.com', 'john.brahim@maistering.com', 'John', 'Brahim', '$2a$12$0i7PVFNJuYx8JeFSB8BPf.1U1O6t9C22Dxx.UMch6Q37aNrrmKnAy', '+3455512334', 0, 0, 0);
-- Kafka Proxy Permissions
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-310, -24 , -22, -301);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-311, -24 , -22, -302);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-312, -24 , -22, -303);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-313, -24 , -22, -304);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-314, -24 , -22, -305);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-315, -24 , -22, -306);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-316, -24 , -22, -307);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-317, -24 , -22, -308);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-318, -24 , -22, -309);
-- Fact Manager Permissions
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-320, -54 , -51, -301);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-321, -54 , -51, -302);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-322, -54 , -51, -303);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-323, -54 , -51, -304);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-324, -54 , -51, -305);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-325, -54 , -51, -306);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-326, -54 , -51, -307);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-327, -54 , -51, -308);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-328, -54 , -51, -309);
-- Facts Dashboard
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-330, -94 , -91, -301);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-331, -94 , -91, -302);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-332, -94 , -91, -303);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-333, -94 , -91, -304);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-334, -94 , -91, -305);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-335, -94 , -91, -306);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-336, -94 , -91, -307);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-337, -94 , -91, -308);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-338, -94 , -91, -309);
-- Infographic Engine
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-340, -74 , -71, -301);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-341, -74 , -71, -302);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-342, -74 , -71, -303);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-343, -74 , -71, -304);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-344, -74 , -71, -305);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-345, -74 , -71, -306);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-346, -74 , -71, -307);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-347, -74 , -71, -308);
INSERT INTO user_roles (id, user_group, role, user_role) VALUES (-348, -74 , -71, -309);