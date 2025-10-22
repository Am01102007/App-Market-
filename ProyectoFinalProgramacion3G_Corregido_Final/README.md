# AppMarket

Estructura del proyecto:

- `appmarket/frontend/` — Aplicación React + Vite
  - Proxy de desarrollo hacia `/api` y usa `VITE_API_URL=/api`.
  - Comandos: `npm run dev`, `npm run build`.
- `appmarket/backend/` — API Spring Boot
  - Configuración en `src/main/resources/application.properties`.
  - Comandos: `./mvnw spring-boot:run` o `mvn spring-boot:run`.
- `logs/` — registros de ejecución.

## Base de datos con Docker Compose

- Archivo `docker-compose.yml` en la raíz.
- Servicio `mysql:8` con credenciales:
  - `root / 1509`
  - BD: `proyecto_final`
- Puerto expuesto: `3306`.
- Volumen persistente: `./db-data`.

Arranque:

- `docker compose up -d`
- Verifica salud: `docker ps` y `docker logs appmarket-mysql`.

## Conexión Frontend–Backend

- Cliente Axios (`frontend/src/lib/api.js`):
  - Base URL: `import.meta.env.VITE_API_URL` → `'/api'` en `.env`.
- Proxy Vite (`frontend/vite.config.js`):
  - `server.proxy['/api']` apunta a `http://localhost:8080` en desarrollo.
- CORS Backend (`backend/.../config/WebConfig.java`):
  - Permite origen `http://localhost:5173` para `'/api/**'`.

## Desarrollo

1. Arranca la base de datos con Docker Compose.
2. Arranca el backend en `http://localhost:8080`.
3. Arranca el frontend en `http://localhost:5173/`.
4. Las llamadas del frontend a `/api/*` se redirigen vía proxy al backend.

## Diseño y UI

- Layout unificado de autenticación (`AuthLayout`) aplicado a Login, Registro y Recuperación.
- Componentes base:
  - `Button` con variantes (`primary`, `secondary`, `ghost`) y accesibilidad.
  - `Input` con `label`, `id`, `error` y `autoComplete`.
- Paleta usada:
  - `neon: #E6FF00`, `navy: #161A23`, `slate: #E9F1FF`, `charcoal: #535862`.

## Notas

- Si el puerto `8080` está ocupado, libera el proceso o cambia `server.port` y ajusta el proxy.
- Las credenciales JDBC del backend están configuradas para MySQL local (puerto 3306) acorde al Compose.