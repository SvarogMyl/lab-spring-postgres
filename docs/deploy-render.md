# Guía de Despliegue en Render (Vía Docker) - Lab Backend

Esta guía detalla el proceso final y exitoso utilizado para desplegar el backend Spring Boot en **Render**.

## 1. Arquitectura del Despliegue
- **Stack**: Java 21 (Spring Boot 3.4.1).
- **Contenedorización**: Docker (Multi-stage build).
- **Plataforma**: Render (Web Service - Free Plan).
- **Base de Datos**: PostgreSQL en Supabase.

## 2. Archivos Clave en el Proyecto
- **Dockerfile**: Localizado en la raíz, maneja la compilación con Maven y la ejecución con JRE 21.
- **application.properties**: Configurado para usar `${PORT:8081}`.
- **SecurityConfig.java**: Configurado para aceptar orígenes desde `${CORS_ALLOWED_ORIGINS}`.

## 3. Pasos de Configuración en Render
1. **Crear Web Service**: Conectar el repositorio de GitHub.
2. **Runtime**: Seleccionar **Docker**.
3. **Región**: `Oregon (US West)`.
4. **Environment Variables**:
   - `DB_HOST`: Host de Supabase.
   - `DB_PORT`: `5432`.
   - `DB_NAME`: `postgres`.
   - `DB_USER`: Usuario de Supabase.
   - `DB_PASSWORD`: Password de Supabase.
   - `JWT_SECRET`: Secreto para tokens.
   - `JWT_EXPIRATION`: `86400000`.
   - `CORS_ALLOWED_ORIGINS`: URL del frontend (inicialmente `http://localhost:3000`).

## 4. Verificación de Salud
El endpoint de salud configurado es `/health`.
- **URL**: `https://lab-spring-postgres.onrender.com/health`
- **Respuesta de éxito**: `{"status":"UP","database":"CONNECTED"}`

## 5. Mantenimiento y Logs
- Cada `push` a la rama `main` disparará un nuevo despliegue automático.
- En el plan gratuito, la aplicación entra en suspensión tras 15 minutos sin tráfico.
