/**
 * Servicio JWT.
 * Genera y valida tokens para autenticación.
 */
package co.edu.uniquindio.ProyectoFinalp3.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * Servicio para manejar operaciones relacionadas con JSON Web Tokens (JWT).
 * Proporciona funcionalidades para generar, validar y extraer información de tokens JWT
 * utilizados para autenticación y autorización en la aplicación.
 * 
 * @author Sistema App Market
 * @version 1.0
 */
@Service
public class JwtService {

    /**
     * Clave secreta utilizada para firmar y verificar los tokens JWT.
     * Se obtiene de la configuración de la aplicación.
     */
    @Value("${jwt.secret}")
    private String secretKey;

    /**
     * Tiempo de expiración de los tokens JWT en milisegundos.
     * Se obtiene de la configuración de la aplicación.
     */
    @Value("${jwt.expiration}")
    private long expirationTime;

    /**
     * Genera un token JWT para un usuario específico.
     * El token incluye el ID del usuario como subject, fecha de emisión y fecha de expiración.
     * 
     * @param userId ID único del usuario para quien se genera el token
     * @return String que representa el token JWT firmado
     */
    public String generateToken(UUID userId) {
        // Validación defensiva del secreto
        String key = (secretKey == null || secretKey.isBlank()) ? "change-me" : secretKey;
        // Usamos el algoritmo HMAC256 para firmar el token
        Algorithm algorithm = Algorithm.HMAC256(key);

        // Generamos el token
        return JWT.create()
                .withSubject(userId.toString()) // Se utiliza el ID del usuario como 'subject'
                .withIssuedAt(new Date()) // Fecha de emisión
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime)) // Fecha de expiración
                .sign(algorithm); // Firmamos el token con el secreto
    }

    /**
     * Extrae el nombre de usuario (subject) del token JWT.
     * 
     * @param token Token JWT del cual extraer el username
     * @return String que representa el username o ID del usuario
     */
    public String extractUsername(String token) {
        DecodedJWT decodedJWT = decodeToken(token);
        return decodedJWT.getSubject(); // Devuelve el 'subject', que es el username o ID del usuario
    }

    /**
     * Verifica si el token JWT ha expirado.
     * 
     * @param token Token JWT a verificar
     * @return true si el token ha expirado, false si aún es válido
     */
    public boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date());
    }

    /**
     * Extrae la fecha de expiración del token JWT.
     * 
     * @param token Token JWT del cual extraer la fecha de expiración
     * @return Date que representa la fecha de expiración del token
     */
    public Date extractExpirationDate(String token) {
        DecodedJWT decodedJWT = decodeToken(token);
        return decodedJWT.getExpiresAt(); // Devuelve la fecha de expiración
    }

    /**
     * Decodifica y verifica un token JWT utilizando la clave secreta.
     * Método privado utilizado internamente por otros métodos del servicio.
     * 
     * @param token Token JWT a decodificar
     * @return DecodedJWT que contiene la información del token decodificado
     * @throws com.auth0.jwt.exceptions.JWTVerificationException si el token es inválido
     */
    private DecodedJWT decodeToken(String token) {
        // Validación defensiva del secreto
        String key = (secretKey == null || secretKey.isBlank()) ? "change-me" : secretKey;
        // Usamos el mismo algoritmo para verificar y decodificar el token
        Algorithm algorithm = Algorithm.HMAC256(key);
        return JWT.require(algorithm)
                .build()
                .verify(token); // Verifica y decodifica el token
    }
    
    /**
     * Extrae el ID del usuario del token JWT.
     * Convierte el subject del token (que contiene el ID como string) a UUID.
     * 
     * @param token Token JWT del cual extraer el ID del usuario
     * @return UUID que representa el ID único del usuario
     * @throws IllegalArgumentException si el subject no es un UUID válido
     */
    public UUID extractUserId(String token) {
        DecodedJWT decodedJWT = decodeToken(token);
        String userIdString = decodedJWT.getSubject(); // Asume que el userId está en el subject
        return UUID.fromString(userIdString); // Convierte el subject a UUID
    }
}
