# Specification: Authentication & Role-Based Access Control (RBAC)

## Overview
Migration from hardcoded authentication to a persistent, database-driven RBAC system using Spring Security, JWT, and PostgreSQL.

## Data Model (ER)

### 1. `users`
- `id`: BIGINT (Primary Key, Auto-increment)
- `username`: VARCHAR(50) (Unique, Not Null)
- `password`: VARCHAR(255) (BCrypt encoded)
- `email`: VARCHAR(100) (Unique, Not Null)
- `enabled`: BOOLEAN (Default: true)

### 2. `roles`
- `id`: BIGINT (Primary Key, Auto-increment)
- `name`: VARCHAR(20) (Unique, Not Null) - e.g., `ROLE_ADMIN`, `ROLE_EDITOR`, `ROLE_VIEWER`

### 3. `user_roles` (Join Table)
- `user_id`: BIGINT (FK to `users.id`)
- `role_id`: BIGINT (FK to `roles.id`)

## Security Logic

### 1. Authentication (`/login`)
- **Input**: `username`, `password`.
- **Process**: 
    1. Load user from database.
    2. Verify password using `BCryptPasswordEncoder`.
    3. Generate JWT containing the username and a list of roles (authorities) in the claims.
- **Output**: JWT Token.

### 2. Authorization
- **Middleware**: `JwtAuthenticationFilter` will extract roles from JWT and populate `SecurityContextHolder`.
- **Annotations**: Use `@PreAuthorize` at the Controller level to restrict access.
    - `GET /items`: Accessible by `ROLE_VIEWER`, `ROLE_EDITOR`, `ROLE_ADMIN`.
    - `POST/PUT /items`: Accessible by `ROLE_EDITOR`, `ROLE_ADMIN`.
    - `DELETE /items`: Accessible only by `ROLE_ADMIN`.

## Migration Plan (Phases)
1. **DB Setup**: Execute SQL scripts to create tables and initial roles/users.
2. **Entities**: Create `User` and `Role` JPA entities.
3. **Repository**: Implement `UserRepository` and `RoleRepository`.
4. **Custom UserDetailsService**: Implementation to load users from DB.
5. **SecurityConfig Update**: Replace hardcoded logic with DB-backed authentication and method-level security.
