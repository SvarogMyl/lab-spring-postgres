# Tareas: Implementación RBAC (Rama dev)

- [x] Definir especificación OpenSpec (`openspec/specs/auth-rbac.md`)
- [x] Configuración de Base de Datos
    - [x] Crear Script SQL de migración (`openspec/specs/rbac-db-setup.sql`)
    - [x] Insertar datos iniciales (Admin, Editor, Viewer)
- [x] Desarrollo Backend
    - [x] Crear Entidades JPA (`User`, `Role`)
    - [x] Crear Repositorios (`UserRepository`, `RoleRepository`)
    - [x] Implementar `CustomUserDetailsService`
    - [x] Actualizar `JwtUtils` para incluir roles en el token
    - [x] Configurar `SecurityConfig` para usar la base de datos y `@PreAuthorize`
- [ ] Pruebas
    - [ ] Verificar login con diferentes usuarios
    - [ ] Validar acceso denegado a DELETE para no-admins
- [ ] Pull Request (IA-Review)
    - [ ] Generar resumen de cambios
    - [ ] Solicitar merge a `main`
