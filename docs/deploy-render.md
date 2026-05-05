# Guía de Despliegue en Render - Lab Backend

Esta guía detalla los pasos para desplegar el backend Spring Boot en la plataforma **Render** utilizando el plan gratuito.

## 1. Requisitos Previos
- Cuenta en [Render.com](https://render.com).
- Repositorio de GitHub conectado a Render.
- Base de Datos PostgreSQL en Supabase operativa.

## 2. Configuración del Web Service en Render
1. Haz clic en **New +** y selecciona **Web Service**.
2. Conecta tu repositorio `lab-spring-postgres`.
3. Configura los siguientes campos:
   - **Name**: `lab-backend` (o el que prefieras).
   - **Environment**: `Java`.
   - **Region**: `Oregon (US West)` (recomendado para proximidad con Supabase).
   - **Branch**: `main`.
   - **Build Command**: `./mvnw clean install -DskipTests`
   - **Start Command**: `java -jar target/demo-0.0.1-SNAPSHOT.jar`
   - **Plan Type**: `Free`.

## 3. Variables de Entorno (Environment Variables)
En la pestaña **Environment** de tu servicio en Render, añade las siguientes variables:

| Variable | Valor |
|---|---|
| `DB_HOST` | Tu host de Supabase |
| `DB_PORT` | `5432` |
| `DB_NAME` | `postgres` |
| `DB_USER` | Tu usuario de Supabase |
| `DB_PASSWORD` | Tu contraseña de Supabase |
| `JWT_SECRET` | Tu secreto JWT (mínimo 32 caracteres) |
| `JWT_EXPIRATION` | `86400000` (24h) |
| `CORS_ALLOWED_ORIGINS` | URL de tu frontend (ej: `https://tu-app.vercel.app`) |

## 4. Verificación
Una vez que el despliegue finalice (puedes ver los logs en Render), verifica el estado de salud:
- URL: `https://tu-app.onrender.com/actuator/health`
- Respuesta esperada: `{"status":"UP"}`

## 5. Notas Importantes
- **Cold Start**: En el plan gratuito, el servicio se "duerme" tras 15 minutos de inactividad. La primera petición puede tardar ~30 segundos en responder.
- **Firebase**: Si no se configura `FIREBASE_CONFIG_PATH`, la app iniciará con un aviso pero funcionará correctamente para el resto de endpoints.
