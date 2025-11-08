FROM node:22-alpine AS frontend-build
# Preparar directorio del frontend
WORKDIR /app/frontend
# Copiar solo manifestos para aprovechar caché de Docker
COPY frontend/package.json frontend/package-lock.json ./
# Instalar dependencias (con tolerancia a peers en npm 7+)
RUN npm ci --no-audit --no-fund --legacy-peer-deps || npm install --no-audit --no-fund --legacy-peer-deps
# Copiar el resto del código del frontend
COPY frontend ./
# Build SPA con base de API relativa al mismo origen
ENV VITE_API_URL=/api
RUN npm run build

FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy only backend sources to keep build context minimal
COPY backend/pom.xml backend/pom.xml
COPY backend/src backend/src

# Include built frontend assets into backend static resources
COPY --from=frontend-build /app/frontend/dist backend/src/main/resources/static

# Build the backend jar (skip tests for faster CI/CD builds)
# Strip UTF-8 BOM from Java sources to avoid javac illegal character errors
RUN find backend/src -type f -name "*.java" -print0 | xargs -0 sed -i '1s/^\xEF\xBB\xBF//'
# Strip UTF-8 BOM from properties to avoid parsing issues
RUN find backend/src/main/resources -type f -name "*.properties" -print0 | xargs -0 sed -i '1s/^\xEF\xBB\xBF//'
# Strip UTF-8 BOM from SQL scripts to avoid init parser issues
RUN find backend/src/main/resources -type f -name "*.sql" -print0 | xargs -0 sed -i '1s/^\xEF\xBB\xBF//'
RUN mvn -f backend/pom.xml -DskipTests package

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Do NOT set SPRING_PROFILES_ACTIVE here; configure it via Render env vars
# Copy built jar from previous stage
COPY --from=build /app/backend/target/*.jar app.jar

EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=postgres
CMD ["java", "-jar", "app.jar"]
