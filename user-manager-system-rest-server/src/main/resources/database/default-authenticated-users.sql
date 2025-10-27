-- noinspection SqlNoDataSourceInspectionForFile

-- User Manager System
INSERT INTO users (id, user_uuid, username, email, name, lastname, password, phone, account_locked, account_blocked, account_expired) VALUES (-1, 'e58ed763-928c-4155-bee9-fdbaaadc15f3', 'admin@test.com', 'admin@test.com', 'Admin', 'Admin', '$2a$10$V84FYKhHogkULl5OlAzw1.mLquRv0Y5kAOtYgUBnv0NuE56KGlBnO', '+34555111222', 0, 0, 0);
INSERT INTO roles (name, description) VALUES ('admin', '');
INSERT INTO roles (name, description) VALUES ('user', '');

INSERT INTO applications (name) VALUES ('UserManagerSystem');
INSERT INTO application_roles (application_name, application_role) VALUES ('UserManagerSystem', 'admin');
INSERT INTO application_roles (application_name, application_role) VALUES ('UserManagerSystem', 'user');
INSERT INTO backend_services (name) VALUES ('UserManagerSystem');
INSERT INTO backend_service_roles (backend_service, name) VALUES ('UserManagerSystem', 'admin');
INSERT INTO backend_service_roles (backend_service, name) VALUES ('UserManagerSystem', 'editor');
INSERT INTO backend_service_roles (backend_service, name) VALUES ('UserManagerSystem', 'viewer');
INSERT INTO application_backend_service_roles (application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES ('UserManagerSystem', 'admin', 'UserManagerSystem', 'admin');
INSERT INTO application_backend_service_roles (application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES ('UserManagerSystem', 'user', 'UserManagerSystem', 'viewer');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-1, 'UserManagerSystem', 'admin', 'UserManagerSystem', 'admin');



-- KafkaProxy
INSERT INTO applications (name) VALUES ('KafkaProxy');
INSERT INTO application_roles (application_name, application_role) VALUES ('KafkaProxy', 'admin');
INSERT INTO application_roles (application_name, application_role) VALUES ('KafkaProxy', 'user');
INSERT INTO backend_services (name) VALUES ('KafkaProxy');
INSERT INTO backend_service_roles (backend_service, name) VALUES ('KafkaProxy', 'admin');
INSERT INTO backend_service_roles (backend_service, name) VALUES ('KafkaProxy', 'editor');
INSERT INTO backend_service_roles (backend_service, name) VALUES ('KafkaProxy', 'viewer');
INSERT INTO application_backend_service_roles (application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES ('KafkaProxy', 'admin', 'KafkaProxy', 'admin');
INSERT INTO application_backend_service_roles (application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES ('KafkaProxy', 'user', 'KafkaProxy', 'viewer');
INSERT INTO application_backend_service_roles (application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES ('KafkaProxy', 'user', 'KafkaProxy', 'editor');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-1, 'KafkaProxy', 'admin', 'KafkaProxy', 'admin');



-- Task Junction
INSERT INTO applications (name) VALUES ('TaskJunction');
INSERT INTO application_roles (application_name, application_role) VALUES ('TaskJunction', 'admin');
INSERT INTO application_roles (application_name, application_role) VALUES ('TaskJunction', 'user');
INSERT INTO backend_services (name) VALUES ('TaskJunction');
INSERT INTO backend_service_roles (backend_service, name) VALUES ('TaskJunction', 'admin');
INSERT INTO backend_service_roles (backend_service, name) VALUES ('TaskJunction', 'editor');
INSERT INTO backend_service_roles (backend_service, name) VALUES ('TaskJunction', 'viewer');
INSERT INTO application_backend_service_roles (application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES ('TaskJunction', 'admin', 'TaskJunction', 'admin');
INSERT INTO application_backend_service_roles (application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES ('TaskJunction', 'user', 'TaskJunction', 'viewer');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-1, 'TaskJunction', 'admin', 'TaskJunction', 'admin');



-- Appointment Center
INSERT INTO applications (name) VALUES ('AppointmentCenter');
INSERT INTO application_roles (application_name, application_role) VALUES ('AppointmentCenter', 'admin');
INSERT INTO application_roles (application_name, application_role) VALUES ('AppointmentCenter', 'user');
INSERT INTO backend_services (name) VALUES ('AppointmentCenter');
INSERT INTO backend_service_roles (backend_service, name) VALUES ('AppointmentCenter', 'admin');
INSERT INTO backend_service_roles (backend_service, name) VALUES ('AppointmentCenter', 'editor');
INSERT INTO backend_service_roles (backend_service, name) VALUES ('AppointmentCenter', 'viewer');
INSERT INTO application_backend_service_roles (application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES ('AppointmentCenter', 'admin', 'AppointmentCenter', 'admin');
INSERT INTO application_backend_service_roles (application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES ('AppointmentCenter', 'user', 'AppointmentCenter', 'viewer');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-1, 'AppointmentCenter', 'admin', 'AppointmentCenter', 'admin');



-- Fact Manager
INSERT INTO applications (name) VALUES ('FactManager');
INSERT INTO application_roles (application_name, application_role) VALUES ('FactManager', 'admin');
INSERT INTO application_roles (application_name, application_role) VALUES ('FactManager', 'user');
INSERT INTO backend_services (name) VALUES ('FactManager');
INSERT INTO backend_service_roles (backend_service, name) VALUES ('FactManager', 'admin');
INSERT INTO backend_service_roles (backend_service, name) VALUES ('FactManager', 'editor');
INSERT INTO backend_service_roles (backend_service, name) VALUES ('FactManager', 'viewer');
INSERT INTO application_backend_service_roles (application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES ('FactManager', 'admin', 'FactManager', 'admin');
INSERT INTO application_backend_service_roles (application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES ('FactManager', 'user', 'FactManager', 'viewer');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-1, 'FactManager', 'admin', 'FactManager', 'admin');



-- Base Form Drools Engine
INSERT INTO applications (name) VALUES ('BaseFormDroolsEngine');
INSERT INTO application_roles (application_name, application_role) VALUES ('BaseFormDroolsEngine', 'admin');
INSERT INTO application_roles (application_name, application_role) VALUES ('BaseFormDroolsEngine', 'user');
INSERT INTO backend_services (name) VALUES ('BaseFormDroolsEngine');
INSERT INTO backend_service_roles (backend_service, name) VALUES ('BaseFormDroolsEngine', 'admin');
INSERT INTO backend_service_roles (backend_service, name) VALUES ('BaseFormDroolsEngine', 'editor');
INSERT INTO backend_service_roles (backend_service, name) VALUES ('BaseFormDroolsEngine', 'viewer');
INSERT INTO application_backend_service_roles (application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES ('BaseFormDroolsEngine', 'admin', 'BaseFormDroolsEngine', 'admin');
INSERT INTO application_backend_service_roles (application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES ('BaseFormDroolsEngine', 'user', 'BaseFormDroolsEngine', 'viewer');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-1, 'BaseFormDroolsEngine', 'admin', 'BaseFormDroolsEngine', 'admin');



-- InfographicEngine
INSERT INTO applications (name) VALUES ('InfographicEngine');
INSERT INTO application_roles (application_name, application_role) VALUES ('InfographicEngine', 'admin');
INSERT INTO application_roles (application_name, application_role) VALUES ('InfographicEngine', 'user');
INSERT INTO backend_services (name) VALUES ('InfographicEngine');
INSERT INTO backend_service_roles (backend_service, name) VALUES ('InfographicEngine', 'admin');
INSERT INTO backend_service_roles (backend_service, name) VALUES ('InfographicEngine', 'editor');
INSERT INTO backend_service_roles (backend_service, name) VALUES ('InfographicEngine', 'viewer');
INSERT INTO application_backend_service_roles (application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES ('InfographicEngine', 'admin', 'InfographicEngine', 'admin');
INSERT INTO application_backend_service_roles (application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES ('InfographicEngine', 'user', 'InfographicEngine', 'viewer');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-1, 'InfographicEngine', 'admin', 'InfographicEngine', 'admin');



-- Knowledge System
INSERT INTO applications (name) VALUES ('KnowledgeSystem');
INSERT INTO application_roles (application_name, application_role) VALUES ('KnowledgeSystem', 'admin');
INSERT INTO application_roles (application_name, application_role) VALUES ('KnowledgeSystem', 'user');
INSERT INTO backend_services (name) VALUES ('KnowledgeSystem');
INSERT INTO backend_service_roles (backend_service, name) VALUES ('KnowledgeSystem', 'admin');
INSERT INTO backend_service_roles (backend_service, name) VALUES ('KnowledgeSystem', 'editor');
INSERT INTO backend_service_roles (backend_service, name) VALUES ('KnowledgeSystem', 'viewer');
INSERT INTO application_backend_service_roles (application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES ('KnowledgeSystem', 'admin', 'KnowledgeSystem', 'admin');
INSERT INTO application_backend_service_roles (application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES ('KnowledgeSystem', 'user', 'KnowledgeSystem', 'viewer');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-1, 'KnowledgeSystem', 'admin', 'KnowledgeSystem', 'admin');



-- Facts Dashboard
INSERT INTO applications (name) VALUES ('FactsDashboard');
INSERT INTO roles (name, description) VALUES ('ceo', '');
INSERT INTO roles (name, description) VALUES ('teamleader', '');
INSERT INTO roles (name, description) VALUES ('employee', '');
INSERT INTO application_roles (application_name, application_role) VALUES ('FactsDashboard', 'ceo');
INSERT INTO application_roles (application_name, application_role) VALUES ('FactsDashboard', 'teamleader');
INSERT INTO application_roles (application_name, application_role) VALUES ('FactsDashboard', 'employee');
INSERT INTO application_backend_service_roles (application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES ('FactsDashboard', 'ceo', 'InfographicEngine', 'viewer');
INSERT INTO application_backend_service_roles (application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES ('FactsDashboard', 'ceo', 'FactManager', 'admin');
INSERT INTO application_backend_service_roles (application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES ('FactsDashboard', 'teamleader', 'InfographicEngine', 'viewer');
INSERT INTO application_backend_service_roles (application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES ('FactsDashboard', 'teamleader', 'FactManager', 'admin');
INSERT INTO application_backend_service_roles (application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES ('FactsDashboard', 'employee', 'InfographicEngine', 'viewer');
INSERT INTO application_backend_service_roles (application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES ('FactsDashboard', 'employee', 'FactManager', 'viewer');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-1, 'FactsDashboard', 'ceo', 'InfographicEngine', 'viewer');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-1, 'FactsDashboard', 'ceo', 'FactManager', 'admin');



-- Card Game
INSERT INTO applications (name) VALUES ('CardGame');
INSERT INTO application_roles (application_name, application_role) VALUES ('CardGame', 'user');
INSERT INTO application_backend_service_roles (application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES ('CardGame', 'user', 'FactManager', 'editor');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-1, 'CardGame', 'user', 'FactManager', 'editor');


-- BiiT Surveys
INSERT INTO applications (name) VALUES ('BiitSurveys');
INSERT INTO application_roles (application_name, application_role) VALUES ('BiitSurveys', 'user');
INSERT INTO application_backend_service_roles (application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES ('BiitSurveys', 'user', 'FactManager', 'editor');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-1, 'BiitSurveys', 'user', 'FactManager', 'editor');



-- Demo Users
INSERT INTO users (id, user_uuid, username, email, name, lastname, password, phone, account_locked, account_blocked, account_expired) VALUES (-100, '15fca0dd-dd89-41dd-9448-6deb4211c204', 'customer1@test.com', 'customer1@test.com', 'Tom', 'Smith', '$2a$10$V84FYKhHogkULl5OlAzw1.mLquRv0Y5kAOtYgUBnv0NuE56KGlBnO', '+345551234', 0, 0, 0);
INSERT INTO users (id, user_uuid, username, email, name, lastname, password, phone, account_locked, account_blocked, account_expired) VALUES (-101, 'aa727351-210b-43f7-bc16-6d023ddf31b5', 'customer2@test.com', 'customer2@test.com', 'Dick', 'Taylor', '$2a$10$V84FYKhHogkULl5OlAzw1.mLquRv0Y5kAOtYgUBnv0NuE56KGlBnO', '+345552234', 0, 0, 0);
INSERT INTO users (id, user_uuid, username, email, name, lastname, password, phone, account_locked, account_blocked, account_expired) VALUES (-102, '87a580f7-4449-4b97-baee-c9f27b65ce4b', 'customer3@test.com', 'customer3@test.com', 'Harry', 'Evans', '$2a$10$V84FYKhHogkULl5OlAzw1.mLquRv0Y5kAOtYgUBnv0NuE56KGlBnO', '+345553234', 0, 0, 0);
INSERT INTO users (id, user_uuid, username, email, name, lastname, password, phone, account_locked, account_blocked, account_expired) VALUES (-103, 'd9166f80-28af-4c2b-a34a-e5b3ee37f5aa', 'customer4@test.com', 'customer4@test.com', 'Tizio', 'Russo', '$2a$10$V84FYKhHogkULl5OlAzw1.mLquRv0Y5kAOtYgUBnv0NuE56KGlBnO', '+345554234', 0, 0, 0);
INSERT INTO users (id, user_uuid, username, email, name, lastname, password, phone, account_locked, account_blocked, account_expired) VALUES (-104, '46cba6a8-f2ab-4d18-a6c3-5f1a1bcae731', 'customer5@test.com', 'customer5@test.com', 'Caio', 'Fabbro', '$2a$10$V84FYKhHogkULl5OlAzw1.mLquRv0Y5kAOtYgUBnv0NuE56KGlBnO', '+345555234', 0, 0, 0);
INSERT INTO users (id, user_uuid, username, email, name, lastname, password, phone, account_locked, account_blocked, account_expired) VALUES (-105, '47d251cf-4238-4582-b6cc-18062eacc2e8', 'customer6@test.com', 'customer6@test.com', 'Sempronio', 'Colombo', '$2a$10$V84FYKhHogkULl5OlAzw1.mLquRv0Y5kAOtYgUBnv0NuE56KGlBnO', '+345556234', 0, 0, 0);
INSERT INTO users (id, user_uuid, username, email, name, lastname, password, phone, account_locked, account_blocked, account_expired) VALUES (-106, 'dfc9c6e1-e99a-4656-bea2-75aeb04e30db', 'customer7@test.com', 'customer7@test.com', 'Fulano', 'Fernández', '$2a$10$V84FYKhHogkULl5OlAzw1.mLquRv0Y5kAOtYgUBnv0NuE56KGlBnO', '+345557234', 0, 0, 0);
INSERT INTO users (id, user_uuid, username, email, name, lastname, password, phone, account_locked, account_blocked, account_expired) VALUES (-107, 'b870b6e4-688f-4271-bfde-f18523efa03c', 'customer8@test.com', 'customer8@test.com', 'Mengano', 'López', '$2a$10$V84FYKhHogkULl5OlAzw1.mLquRv0Y5kAOtYgUBnv0NuE56KGlBnO', '+345557235', 0, 0, 0);
INSERT INTO users (id, user_uuid, username, email, name, lastname, password, phone, account_locked, account_blocked, account_expired) VALUES (-108, 'd752ff38-3ccd-4063-a7f8-5e5aec12eac3', 'customer9@test.com', 'customer9@test.com', 'Zutano', 'Rodríguez', '$2a$10$V84FYKhHogkULl5OlAzw1.mLquRv0Y5kAOtYgUBnv0NuE56KGlBnO', '+345557236', 0, 0, 0);

-- Kafka Proxy Permissions
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-100, 'KafkaProxy', 'user', 'KafkaProxy', 'viewer');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-101, 'KafkaProxy', 'user', 'KafkaProxy', 'viewer');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-102, 'KafkaProxy', 'user', 'KafkaProxy', 'viewer');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-103, 'KafkaProxy', 'user', 'KafkaProxy', 'viewer');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-104, 'KafkaProxy', 'user', 'KafkaProxy', 'viewer');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-105, 'KafkaProxy', 'user', 'KafkaProxy', 'viewer');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-100, 'KafkaProxy', 'user', 'KafkaProxy', 'editor');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-101, 'KafkaProxy', 'user', 'KafkaProxy', 'editor');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-102, 'KafkaProxy', 'user', 'KafkaProxy', 'editor');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-103, 'KafkaProxy', 'user', 'KafkaProxy', 'editor');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-104, 'KafkaProxy', 'user', 'KafkaProxy', 'editor');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-105, 'KafkaProxy', 'user', 'KafkaProxy', 'editor');

-- Task Junction
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-100, 'TaskJunction', 'admin', 'TaskJunction', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-101, 'TaskJunction', 'admin', 'TaskJunction', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-102, 'TaskJunction', 'admin', 'TaskJunction', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-103, 'TaskJunction', 'admin', 'TaskJunction', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-104, 'TaskJunction', 'admin', 'TaskJunction', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-105, 'TaskJunction', 'admin', 'TaskJunction', 'admin');

-- Appointment Center
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-100, 'AppointmentCenter', 'admin', 'AppointmentCenter', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-101, 'AppointmentCenter', 'admin', 'AppointmentCenter', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-102, 'AppointmentCenter', 'admin', 'AppointmentCenter', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-103, 'AppointmentCenter', 'admin', 'AppointmentCenter', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-104, 'AppointmentCenter', 'admin', 'AppointmentCenter', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-105, 'AppointmentCenter', 'admin', 'AppointmentCenter', 'admin');

-- Fact Manager Permissions
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-100, 'FactManager', 'admin', 'FactManager', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-101, 'FactManager', 'admin', 'FactManager', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-102, 'FactManager', 'admin', 'FactManager', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-103, 'FactManager', 'admin', 'FactManager', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-104, 'FactManager', 'admin', 'FactManager', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-105, 'FactManager', 'admin', 'FactManager', 'admin');

-- BaseForm Drools Engine
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-100, 'BaseFormDroolsEngine', 'admin', 'BaseFormDroolsEngine', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-101, 'BaseFormDroolsEngine', 'admin', 'BaseFormDroolsEngine', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-102, 'BaseFormDroolsEngine', 'admin', 'BaseFormDroolsEngine', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-103, 'BaseFormDroolsEngine', 'admin', 'BaseFormDroolsEngine', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-104, 'BaseFormDroolsEngine', 'admin', 'BaseFormDroolsEngine', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-105, 'BaseFormDroolsEngine', 'admin', 'BaseFormDroolsEngine', 'admin');

-- Facts Dashboard
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-100, 'FactsDashboard', 'ceo', 'FactManager', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-101, 'FactsDashboard', 'ceo', 'FactManager', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-102, 'FactsDashboard', 'ceo', 'FactManager', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-103, 'FactsDashboard', 'ceo', 'FactManager', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-104, 'FactsDashboard', 'ceo', 'FactManager', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-105, 'FactsDashboard', 'ceo', 'FactManager', 'admin');

-- Infographic Engine
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-100, 'InfographicEngine', 'admin', 'InfographicEngine', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-101, 'InfographicEngine', 'admin', 'InfographicEngine', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-102, 'InfographicEngine', 'admin', 'InfographicEngine', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-103, 'InfographicEngine', 'admin', 'InfographicEngine', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-104, 'InfographicEngine', 'admin', 'InfographicEngine', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-105, 'InfographicEngine', 'admin', 'InfographicEngine', 'admin');

-- Knowledge System
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-100, 'KnowledgeSystem', 'admin', 'KnowledgeSystem', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-101, 'KnowledgeSystem', 'admin', 'KnowledgeSystem', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-102, 'KnowledgeSystem', 'admin', 'KnowledgeSystem', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-103, 'KnowledgeSystem', 'admin', 'KnowledgeSystem', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-104, 'KnowledgeSystem', 'admin', 'KnowledgeSystem', 'admin');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-105, 'KnowledgeSystem', 'admin', 'KnowledgeSystem', 'admin');

-- Card Game
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-100, 'CardGame', 'user', 'FactManager', 'editor');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-101, 'CardGame', 'user', 'FactManager', 'editor');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-102, 'CardGame', 'user', 'FactManager', 'editor');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-103, 'CardGame', 'user', 'FactManager', 'editor');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-104, 'CardGame', 'user', 'FactManager', 'editor');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-105, 'CardGame', 'user', 'FactManager', 'editor');

-- Demo Users
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-100, 'BiitSurveys', 'user', 'FactManager', 'editor');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-101, 'BiitSurveys', 'user', 'FactManager', 'editor');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-102, 'BiitSurveys', 'user', 'FactManager', 'editor');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-103, 'BiitSurveys', 'user', 'FactManager', 'editor');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-104, 'BiitSurveys', 'user', 'FactManager', 'editor');
INSERT INTO users_by_application_backend_service_roles (user_id, application_role_application, application_role_role, backend_service_role_service, backend_service_role_name) VALUES (-105, 'BiitSurveys', 'user', 'FactManager', 'editor');
