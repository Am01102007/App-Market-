/**
 * Controlador de usuarios.
 * Administra registro, consulta y actualización del perfil.
 */
package co.edu.uniquindio.ProyectoFinalp3.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import co.edu.uniquindio.ProyectoFinalp3.models.User;
import co.edu.uniquindio.ProyectoFinalp3.services.JwtService;
import co.edu.uniquindio.ProyectoFinalp3.services.UserService;

/**
 * Controlador REST para manejar operaciones relacionadas con usuarios.
 * Proporciona endpoints para buscar, obtener y actualizar información de usuarios.
 * 
 * @author Sistema App Market
 * @version 1.0
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    /**
     * Servicio para manejar operaciones relacionadas con usuarios.
     */
    @Autowired
    private UserService userService;

    /**
     * Busca usuarios por nombre de usuario.
     * 
     * @param username El nombre de usuario a buscar
     * @return Lista de usuarios que coinciden con el criterio de búsqueda
     */
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsersByUsername(@RequestParam String username) {
        List<User> users = userService.searchUsersByUsername(username);
        return ResponseEntity.ok(users);
    }
    
    /**
     * Obtiene un usuario por su ID único.
     * 
     * @param userId El ID único del usuario
     * @return El usuario encontrado o 404 si no existe
     */
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable UUID userId) {
        Optional<User> user = userService.getUserById(userId);
        return user.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @Autowired
private JwtService jwtService;

// Endpoint para actualizar la información del usuario autenticado
@PatchMapping("/{userId}/update")
public ResponseEntity<String> updateUserInfo(
        @PathVariable UUID userId,
        @RequestBody User updatedUserInfo,
        @RequestHeader("Authorization") String authorizationHeader) {

    // Verifica si el encabezado tiene el token en el formato correcto
    if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
        return ResponseEntity.status(401).body("No autorizado: el token está ausente o es incorrecto.");
    }

    // Extrae el token eliminando el prefijo "Bearer "
    String token = authorizationHeader.substring(7);

    // Obtener el ID del usuario autenticado desde el token
    UUID authenticatedUserId;
    try {
        authenticatedUserId = jwtService.extractUserId(token);
    } catch (Exception e) {
        return ResponseEntity.status(401).body("Token inválido o expirado.");
    }

    // Verifica si el userId del token coincide con el userId en el path
    if (!userId.equals(authenticatedUserId)) {
        return ResponseEntity.status(403).body("No tienes permiso para actualizar esta información.");
    }

    // Intenta actualizar la información del usuario
    boolean updated = userService.updateUserInfo(userId, updatedUserInfo);
    if (updated) {
        return ResponseEntity.ok("Información actualizada exitosamente.");
    } else {
        return ResponseEntity.status(404).body("Usuario no encontrado.");
    }
}
}
