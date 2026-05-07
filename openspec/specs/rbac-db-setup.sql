-- Specification: RBAC Database Schema
-- Project: lab-spring-postgres
-- Standard: PostgreSQL 16+

-- 1. Roles Table
CREATE TABLE IF NOT EXISTS roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20) UNIQUE NOT NULL
);

-- 2. Users Table
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    enabled BOOLEAN DEFAULT TRUE
);

-- 3. Many-to-Many Relationship (User <-> Role)
CREATE TABLE IF NOT EXISTS user_roles (
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    role_id INT REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

-- 4. Initial Data (Seeds)
INSERT INTO roles (name) VALUES ('ROLE_ADMIN') ON CONFLICT DO NOTHING;
INSERT INTO roles (name) VALUES ('ROLE_EDITOR') ON CONFLICT DO NOTHING;
INSERT INTO roles (name) VALUES ('ROLE_VIEWER') ON CONFLICT DO NOTHING;

-- Initial Admin User (password: admin123)
-- BCrypt: $2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOn2
INSERT INTO users (username, password, email) 
VALUES ('admin', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOn2', 'admin@example.com')
ON CONFLICT DO NOTHING;

-- Assign Admin Role
INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN'
ON CONFLICT DO NOTHING;
