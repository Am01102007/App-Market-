package co.edu.uniquindio.ProyectoFinalp3.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración Web MVC
 * Propósito: Define la política CORS para permitir que el frontend (Vite en
 * desarrollo) acceda de forma segura a los endpoints del backend.
 *
 * Contexto: Durante desarrollo, el frontend corre en `http://localhost:5173`
 * y las solicitudes se envían a rutas bajo `/api/**`. Esta clase permite el
 * intercambio de recursos entre ambos orígenes.
 *
 * Autor: Estudiante de Ingeniería de Sistemas (6º semestre)
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Registra las reglas CORS.
     * Permite el origen de desarrollo del frontend y métodos comunes HTTP.
     * Incluye credenciales para escenarios donde se usa autenticación.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
