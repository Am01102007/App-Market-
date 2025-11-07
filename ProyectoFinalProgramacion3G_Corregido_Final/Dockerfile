FROM node:18-alpine AS frontend-build
WORKDIR /app
# Copy frontend and build static assets
COPY frontend ./frontend
WORKDIR /app/frontend
# Build SPA with API base relative to same origin
ENV VITE_API_URL=/api
RUN npm ci && npm run build

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

