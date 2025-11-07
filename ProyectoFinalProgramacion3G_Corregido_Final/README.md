# AppMarket

Estructura del proyecto (canónica):

- `frontend/`  Aplicación React + Vite
  - Proxy de desarrollo hacia `/api` y usa `VITE_API_URL=/api`.
  - Comandos: `npm ci && npm run dev`, `npm run build`.
- `backend/`  API Spring Boot
  - Configuración por perfiles en `src/main/resources/application-*.properties`.
  - Perfiles disponibles: `dev` (MySQL), `h2` (memoria), `postgres` (Render).
  - Comandos (requiere Maven): `mvn spring-boot:run -Dspring-boot.run.profiles=dev`.
  - Alternativa sin Maven: usar Docker (ver más abajo).

Carpetas antiguas y no usadas: ya eliminadas (`appmarket/*` y `ProyectoFinal/*`).

## Base de datos con Docker Compose (opcional para perfil `dev`)

- Archivo `docker-compose.yml` en la raíz.
- Servicio `mysql:8` con credenciales:
  - `root / 1509`
  - BD: `proyecto_final`
- Puerto expuesto: `3306`.
- Volumen persistente: `./db-data`.

Arranque de MySQL:

- `docker compose up -d`
- Verifica salud: `docker ps` y `docker logs appmarket-mysql`.

## Conexión FrontendBackend

- Cliente Axios (`frontend/src/lib/api.js`):
  - Base URL: `import.meta.env.VITE_API_URL`  `'/api'` en `.env`.
- Proxy Vite (`frontend/vite.config.js`):
  - `server.proxy['/api']` apunta a `http://localhost:8080` en desarrollo.
- CORS Backend (`backend/.../config/WebConfig.java`):
  - Orígenes configurables vía `ALLOWED_ORIGINS` (coma-separados). Por defecto incluye `http://localhost:5173`.

## Desarrollo

1. Frontend
   - `cd frontend`
   - `npm ci`
   - `npm run dev`  `http://localhost:5173/`
2. Backend (con Maven instalado)
   - `cd backend`
   - Perfil H2 (rápido, sin DB externa):
     - `mvn spring-boot:run -Dspring-boot.run.profiles=h2`
   - Perfil MySQL (requiere Docker Compose arriba):
     - `mvn spring-boot:run -Dspring-boot.run.profiles=dev`
3. Backend sin Maven (Docker)
   - Build: `docker build -t appmarket-backend .`
   - Run (H2): `docker run --rm -p 8080:8080 -e SPRING_PROFILES_ACTIVE=h2 appmarket-backend`
4. Las llamadas del frontend a `/api/*` se redirigen vía proxy al backend.

## Despliegue en Render + PostgreSQL (Neon)

- Perfil de Spring activo: `postgres`.
- Variables de entorno en Render (Dashboard o `render.yaml`):
  - `SPRING_PROFILES_ACTIVE=postgres`
  - `SPRING_DATASOURCE_URL=jdbc:postgresql://ep-broad-cake-a4jr924o-pooler.us-east-1.aws.neon.tech/neondb?sslmode=require&channel_binding=require`
  - `SPRING_DATASOURCE_USERNAME=neondb_owner`
  - `SPRING_DATASOURCE_PASSWORD=<tu_password_de_neon>`
  - `ALLOWED_ORIGINS=https://appmarket-frontend.onrender.com,http://localhost:5173`
  - `JWT_SECRET=<auto o manual>`
  - `JWT_EXPIRATION=86400000`
- Frontend (Static Site):
  - `VITE_API_URL=https://appmarket-backend.onrender.com/api`
- Referencia: `https://render.com/docs/configure-environment-variables`.

## Diseño y UI

- Layout unificado de autenticación (`AuthLayout`) aplicado a Login, Registro y Recuperación.
- Componentes base:
  - `Button` con variantes (`primary`, `secondary`, `ghost`) y accesibilidad.

## Estilo de comentarios (Javadoc)

- Idioma: todo comentario debe estar en español.
- Persona: se escribe en tercera persona, evitando primera persona.
- Estilo: simple y claro, frases cortas y directas.
- Objetivo: explicar qué hace la clase o método, no cómo implementarlo.
- Ejemplos:
  - Clase: "Servicio que gestiona operaciones de usuarios. Incluye autenticación y registro."
  - Método: "Inicia sesión verificando correo y contraseña. Devuelve un token si es válido."
- Ámbito: se prioriza documentar controladores, servicios, configuraciones y modelos.
- Cohesión: los comentarios deben ser consistentes en todo el proyecto.
  - `Input` con `label`, `id`, `error` y `autoComplete`.
- Paleta usada:
  - `neon: #E6FF00`, `navy: #161A23`, `slate: #E9F1FF`, `charcoal: #535862`.

## Notas

- Si el puerto `8080` está ocupado, libera el proceso o cambia `server.port` y ajusta el proxy.
- En producción, usa Neon (PostgreSQL) y Render con el perfil `postgres`. Configura las variables anteriores para una conexión segura.

## Problemas comunes y soluciones

- 401 en `POST /api/auth/login`:
  - Email inexistente o contraseña incorrecta.
  - Si se usan usuarios demo creados con contraseña en texto plano, el login fallará. Solución: crear usuarios vía registro o actualizar la columna `password` con un hash BCrypt válido.
- 400 en `POST /api/auth/register`:
  - Cuerpo inválido o faltan campos requeridos. Verifica `email` y `password` y formatos.
- 409 en `POST /api/auth/register`:
  - Email ya existe (`existsByEmail`). Usa otro correo.
- 500 en `POST /api/auth/login`:
  - Normalmente por contraseñas no encriptadas o `JWT_SECRET` vacío/incorrecto. Revisa logs y variables de entorno en Render.

## CORS y VITE_API_URL

- Backend (`WebConfig.java`):
  - `ALLOWED_ORIGINS` acepta una lista separada por comas. Incluye tu dominio de frontend de Render y `http://localhost:5173`.
  - Ejemplo: `ALLOWED_ORIGINS=https://appmarket-frontend.onrender.com,http://localhost:5173`.
- Frontend (Render):
  - `VITE_API_URL` debe apuntar al backend: `https://<tu-backend>.onrender.com/api`.
- Verificación rápida (DevTools navegador):
  - El preflight `OPTIONS` responde 200.
  - Las peticiones a `/api/*` no muestran errores de CORS.

## Checklist de producción

- Backend Render:
  - `SPRING_PROFILES_ACTIVE=postgres`.
  - Configura `SPRING_DATASOURCE_*` correctos (Neon con SSL).
  - `ALLOWED_ORIGINS` incluye tu frontend.
  - `JWT_SECRET` definido y `JWT_EXPIRATION` válido.
- Frontend Render:
  - `VITE_API_URL` exacto a `https://<backend>/api`.
- Datos:
  - Evita usuarios demo con contraseñas en texto plano. Usa registro o hashes BCrypt.
