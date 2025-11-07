/**
 * Configuración de seguridad HTTP.
 * Define reglas de autorización y beans relacionados con autenticación.
 */
package co.edu.uniquindio.ProyectoFinalp3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// Importaciones de seguridad
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    // Comentado temporalmente para debugging
    // @Bean
    // public JwtFilter jwtFilter() {
    //     return new JwtFilter(); // Instancia del filtro JWT
    // }

    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Deshabilita CSRF si no lo necesitas
                .cors().and()
                .authorizeHttpRequests()
                .requestMatchers("/api/auth/**").permitAll() // Rutas de autenticación abiertas
                .requestMatchers("/api/products/**").permitAll() // Permitir acceso público a todos los endpoints de productos
                .requestMatchers("/orders/**").permitAll()
                .requestMatchers("/contacts/**").permitAll()
                .requestMatchers("/payments/**").permitAll()
                .requestMatchers("/ws/chat").permitAll() 
                .requestMatchers("/ws/**").permitAll() 
                .requestMatchers("/users/**").permitAll()
                .anyRequest().permitAll() // Permitir acceso a todas las rutas temporalmente para debugging
                .and();
                // Comentamos temporalmente el filtro JWT para debugging
                //.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Provee el gestor de autenticación.
     * Obtiene la instancia desde la configuración de Spring.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Provee el codificador de contraseñas.
     * Utiliza BCrypt para almacenar contraseñas de forma segura.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
