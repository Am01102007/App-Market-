/**
 * Configuración de base de datos con fallback automático.
 * 
 * Esta clase implementa un mecanismo de fallback que intenta conectar primero
 * a MySQL y, en caso de fallo, automáticamente cambia a H2 para desarrollo.
 * 
 * @author Equipo de desarrollo AppMarket
 * @version 1.0.0
 * @since 2024
 */
/**
 * Configuración de base de datos.
 * Define propiedades y beans para el acceso a datos.
 */
package co.edu.uniquindio.ProyectoFinalp3.config;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class DatabaseConfig {
    private static final Logger log = LoggerFactory.getLogger(DatabaseConfig.class);

    private final Environment environment;

    public DatabaseConfig(Environment environment) {
        this.environment = environment;
    }

    /**
     * Configuración del DataSource con fallback automático.
     * 
     * Intenta conectar a MySQL primero, y si falla, automáticamente
     * configura H2 como base de datos de fallback.
     * 
     * @return DataSource configurado (MySQL o H2 según disponibilidad)
     */
    @Bean
    @Primary
    @Profile("dev")
    public DataSource dataSource() {
        // Usar exclusivamente MySQL (contenedor Docker) en desarrollo
        DataSource mysqlDataSource = createMySQLDataSource();
        log.info("✅ Usando MySQL (Docker) para desarrollo");
        return mysqlDataSource;
    }

    /**
     * Crea el DataSource para MySQL.
     * 
     * @return DataSource configurado para MySQL
     */
    private DataSource createMySQLDataSource() {
        // Leer configuración desde application-*.properties para evitar discrepancias
        String driver = environment.getProperty("spring.datasource.driver-class-name", "com.mysql.cj.jdbc.Driver");
        String url = environment.getProperty(
                "spring.datasource.url",
                // Valor por defecto alineado con docker-compose (localhost:3307, DB proyecto_final)
                "jdbc:mysql://localhost:3307/proyecto_final?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
        );
        String username = environment.getProperty("spring.datasource.username", "root");
        String password = environment.getProperty("spring.datasource.password", "");

        return DataSourceBuilder.create()
                .driverClassName(driver)
                .url(url)
                .username(username)
                .password(password)
                .build();
    }

    // Eliminado el fallback a H2: solo conexión a MySQL

    /**
     * DataSource específico para producción (solo MySQL).
     * 
     * @return DataSource configurado para MySQL en producción
     */
    @Bean
    @Primary
    @Profile("prod")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource productionDataSource() {
        log.info("🚀 Configurando MySQL para producción");
        return DataSourceBuilder.create().build();
    }

    /**
     * DataSource específico para PostgreSQL (Render).
     * Lee parámetros desde application-postgres.properties o variables de entorno.
     */
    @Bean
    @Primary
    @Profile("postgres")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource postgresDataSource() {
        String url = environment.getProperty("spring.datasource.url");
        String normalizedUrl = normalizeJdbcUrl(url);
        String user = environment.getProperty("spring.datasource.username");
        String password = environment.getProperty("spring.datasource.password");
        String driver = environment.getProperty("spring.datasource.driver-class-name", "org.postgresql.Driver");
        if (url == null || url.isBlank()) {
            log.error("❌ spring.datasource.url no está definido. Configure SPRING_DATASOURCE_URL/JDBC_DATABASE_URL/DATABASE_URL en Render.");
        } else {
            if (!url.equals(normalizedUrl)) {
                log.info("� Normalizando URL Postgres a JDBC. original={}, jdbc={}", url, normalizedUrl);
            } else {
                log.info("� Configurando PostgreSQL (url={}, user={})", normalizedUrl, user);
            }
        }
        // Fallback: si usuario/clave no están en propiedades, intentar extraerlos de la URL libpq
        if ((user == null || user.isBlank() || password == null || password.isBlank()) && url != null && !url.isBlank()) {
            String[] creds = extractCredentialsFromUrl(url);
            if (creds != null) {
                if ((user == null || user.isBlank()) && creds[0] != null) {
                    user = creds[0];
                    log.info("👤 Usuario obtenido de URL: {}", user);
                }
                if ((password == null || password.isBlank()) && creds[1] != null) {
                    password = creds[1];
                    log.info("🔑 Password obtenido de URL (oculto)");
                }
            }
        }

        return DataSourceBuilder.create()
                .driverClassName(driver)
                .url(normalizedUrl)
                .username(user)
                .password(password)
                .build();
    }

    /**
     * Normaliza una URL de Postgres de formato libpq (postgres:// o postgresql://)
     * al formato JDBC (jdbc:postgresql://). Además, garantiza que sslmode=require
     * esté presente y elimina parámetros no soportados como channel_binding.
     */
    private String normalizeJdbcUrl(String rawUrl) {
        if (rawUrl == null || rawUrl.isBlank()) {
            return rawUrl;
        }

        String url = rawUrl.trim();
        // Si ya es JDBC, asegurar sslmode
        if (url.startsWith("jdbc:postgresql://")) {
            return ensureSslMode(url);
        }

        // Convertir esquemas libpq a JDBC
        if (url.startsWith("postgres://") || url.startsWith("postgresql://")) {
            try {
                URI uri = new URI(url);
                String host = uri.getHost();
                int port = uri.getPort(); // -1 si no está especificado
                String path = uri.getPath(); // /dbname
                String db = (path == null) ? "" : path.startsWith("/") ? path.substring(1) : path;

                StringBuilder jdbc = new StringBuilder();
                jdbc.append("jdbc:postgresql://").append(host);
                if (port > -1) {
                    jdbc.append(":").append(port);
                }
                jdbc.append("/").append(db);

                // Parsear query params y normalizar
                Map<String, String> params = new LinkedHashMap<>();
                String query = uri.getQuery();
                if (query != null && !query.isBlank()) {
                    String[] pairs = query.split("&");
                    for (String pair : pairs) {
                        int idx = pair.indexOf('=');
                        if (idx > 0) {
                            String key = pair.substring(0, idx);
                            String value = pair.substring(idx + 1);
                            params.put(key, value);
                        } else if (!pair.isBlank()) {
                            params.put(pair, "");
                        }
                    }
                }

                // Remover parámetros no soportados por JDBC
                params.remove("channel_binding");
                // Asegurar SSL requerido para Neon
                params.putIfAbsent("sslmode", "require");

                if (!params.isEmpty()) {
                    StringBuilder qb = new StringBuilder("?");
                    boolean first = true;
                    for (Map.Entry<String, String> e : params.entrySet()) {
                        if (!first) qb.append('&');
                        qb.append(e.getKey()).append('=').append(e.getValue());
                        first = false;
                    }
                    jdbc.append(qb);
                }

                return jdbc.toString();
            } catch (Exception ex) {
                log.warn("⚠️ No se pudo parsear la URL Postgres; aplicando reemplazo de esquema. error={}", ex.toString());
                String replaced = url.replaceFirst("^postgres(ql)?://", "jdbc:postgresql://");
                return ensureSslMode(replaced);
            }
        }

        // Retornar tal cual si no coincide
        return url;
    }

    private String ensureSslMode(String jdbcUrl) {
        if (jdbcUrl == null || jdbcUrl.isBlank()) return jdbcUrl;
        if (jdbcUrl.contains("sslmode=")) return jdbcUrl;
        // Agregar sslmode=require
        if (jdbcUrl.contains("?")) {
            return jdbcUrl + "&sslmode=require";
        }
        return jdbcUrl + "?sslmode=require";
    }

    /**
     * Extrae credenciales (usuario y password) del componente userinfo de una URL libpq.
     * Retorna {usuario, password} o null si no hay credenciales.
     */
    private String[] extractCredentialsFromUrl(String rawUrl) {
        try {
            URI uri = new URI(rawUrl);
            String userInfo = uri.getUserInfo();
            if (userInfo == null || userInfo.isBlank()) return null;
            String[] parts = userInfo.split(":", 2);
            String u = parts.length > 0 ? parts[0] : null;
            String p = parts.length > 1 ? parts[1] : null;
            return new String[] { u, p };
        } catch (Exception e) {
            log.debug("No se pudieron extraer credenciales de la URL: {}", e.toString());
            return null;
        }
    }
}
