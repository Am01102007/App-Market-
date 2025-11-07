/**
 * Filtro JWT.
 * Extrae y valida tokens en solicitudes HTTP.
 */
package co.edu.uniquindio.ProyectoFinalp3.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import co.edu.uniquindio.ProyectoFinalp3.security.CustomAuthentication;

import java.io.IOException;
// @Component - Comentado temporalmente para debugging

public class JwtFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // No procesar las rutas públicas
        String uri = request.getRequestURI();
        if (uri.startsWith("/api/auth/") || 
            uri.startsWith("/api/products/")) {
            filterChain.doFilter(request, response); // Continua la cadena de filtros sin hacer nada
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            // Valida el token JWT
            if (isValidToken(token)) {
                // Si el token es válido, se establece la autenticación
                SecurityContextHolder.getContext().setAuthentication(getAuthentication(token));
            } else {
                // Si el token no es válido, devuelve un mensaje de error adecuado
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"ok\": false, \"msg\": \"No autenticado\"}");
                return;
            }
        } else {
            // Si no hay token, también devuelve un mensaje de error
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"ok\": false, \"msg\": \"No autenticado\"}");
            return;
        }

        filterChain.doFilter(request, response); // Continúa el filtro
    }

    private boolean isValidToken(String token) {
        try {
            // Crea el verificador con la clave secreta
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey)) // Usa la clave secreta
                    .build();

            // Intenta verificar el token
            DecodedJWT decodedJWT = verifier.verify(token); // Si el token es válido, lo decodifica
            return true; // Si no hay excepción, el token es válido
        } catch (JWTVerificationException e) {
            // Si hay una excepción, el token no es válido
            return false;
        }
    }

    private CustomAuthentication getAuthentication(String token) {
        // Decodifica el token para extraer los datos del usuario
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject(); // Extrae el nombre de usuario del token (si lo tienes)

        // Si el token tiene roles u otros datos, puedes extraerlos aquí (dependiendo de
        // cómo se generó el token)

        return new CustomAuthentication(username);
    }
}
