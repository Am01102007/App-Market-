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

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(); // Instancia del filtro JWT
    }

    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Deshabilita CSRF si no lo necesitas
                .cors().and()
                .authorizeHttpRequests()
                .requestMatchers("/api/auth/**").permitAll() // Rutas de autenticación abiertas
                .requestMatchers("/orders/**").permitAll()
                .requestMatchers("/contacts/**").permitAll()
                .requestMatchers("/payments/**").permitAll()
                .requestMatchers("/ws/chat").permitAll() 
                .requestMatchers("/ws/**").permitAll() 
                .requestMatchers("/users/**").permitAll()
                .requestMatchers("/api/users/**").authenticated()
                .requestMatchers("/api/users/{userId}/update").authenticated()
                .requestMatchers("/api/**").authenticated() // Solo autenticados pueden crear productos
                // .requestMatchers("/api/products/**").permitAll() // Otras rutas de productos
                // son públicas
                .anyRequest().authenticated() // Cualquier otra ruta debe ser autenticada
                .and()
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class); // Asegura que tu filtro JWT
                                                                                           // esté en la cadena de
                                                                                           // filtros

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}