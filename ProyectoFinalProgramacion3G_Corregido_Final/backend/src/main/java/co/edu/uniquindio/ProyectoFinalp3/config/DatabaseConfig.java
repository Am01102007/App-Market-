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

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class DatabaseConfig {

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
        // Intentar MySQL primero
        DataSource mysqlDataSource = createMySQLDataSource();
        
        if (testConnection(mysqlDataSource)) {
            log.info("✅ Conectado exitosamente a MySQL para desarrollo");
            return mysqlDataSource;
        } else {
            log.warn("⚠️ MySQL no disponible, cambiando a H2 como fallback");
            return createH2DataSource();
        }
    }

    /**
     * Crea el DataSource para MySQL.
     * 
     * @return DataSource configurado para MySQL
     */
    private DataSource createMySQLDataSource() {
        return DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url("jdbc:mysql://localhost:3306/proyecto_final_dev?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true")
                .username("root")
                .password("1509")
                .build();
    }

    /**
     * Crea el DataSource para H2.
     * 
     * @return DataSource configurado para H2
     */
    private DataSource createH2DataSource() {
        log.info("🔄 Configurando H2 como base de datos de fallback");
        return DataSourceBuilder.create()
                .driverClassName("org.h2.Driver")
                .url("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
                .username("sa")
                .password("")
                .build();
    }

    /**
     * Prueba la conexión a un DataSource.
     * 
     * @param dataSource DataSource a probar
     * @return true si la conexión es exitosa, false en caso contrario
     */
    private boolean testConnection(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(5); // Timeout de 5 segundos
        } catch (SQLException e) {
            log.debug("Error al conectar a la base de datos: {}", e.getMessage());
            return false;
        }
    }

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
}