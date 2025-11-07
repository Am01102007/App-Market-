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
        String user = environment.getProperty("spring.datasource.username");
        String driver = environment.getProperty("spring.datasource.driver-class-name", "org.postgresql.Driver");
        if (url == null || url.isBlank()) {
            log.error("❌ spring.datasource.url no está definido. Configure SPRING_DATASOURCE_URL/JDBC_DATABASE_URL/DATABASE_URL en Render.");
        } else {
            log.info("🐘 Configurando PostgreSQL (url={}, user={})", url, user);
        }
        return DataSourceBuilder.create()
                .driverClassName(driver)
                .url(url)
                .username(user)
                .password(environment.getProperty("spring.datasource.password"))
                .build();
    }
}
