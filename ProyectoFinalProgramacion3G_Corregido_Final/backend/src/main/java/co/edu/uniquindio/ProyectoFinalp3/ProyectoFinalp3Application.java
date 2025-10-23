package co.edu.uniquindio.ProyectoFinalp3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Clase principal de la aplicación AppMarket Backend.
 * 
 * Esta aplicación Spring Boot proporciona una API REST para un marketplace
 * de productos con funcionalidades de autenticación JWT, gestión de usuarios,
 * productos, contactos y chat en tiempo real.
 * 
 * Configuraciones habilitadas:
 * - JPA Repositories para acceso a datos
 * - JPA Auditing para tracking de cambios
 * - Entity Scan para detectar entidades JPA
 * 
 * @author Sistema App Market
 * @version 1.0
 */
@SpringBootApplication
@EnableJpaRepositories
@EnableJpaAuditing
@EntityScan(basePackages = "co.edu.uniquindio.ProyectoFinalp3.models")
public class ProyectoFinalp3Application {

	/**
	 * Método principal que inicia la aplicación Spring Boot.
	 * 
	 * @param args Argumentos de línea de comandos
	 */
	public static void main(String[] args) {
		SpringApplication.run(ProyectoFinalp3Application.class, args);
	}
}
