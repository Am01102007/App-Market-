package co.edu.uniquindio.ProyectoFinalp3.services;

import co.edu.uniquindio.ProyectoFinalp3.dto.UpdateUserRequest;
import co.edu.uniquindio.ProyectoFinalp3.models.User;
import co.edu.uniquindio.ProyectoFinalp3.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio que gestiona operaciones de usuarios.
 * Incluye autenticación, registro, búsqueda y actualización con contraseñas cifradas.
 */
@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    /**
     * Repositorio para acceder a los datos de usuarios en la base de datos.
     * Se inyecta automáticamente por Spring.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Codificador de contraseñas para encriptar y verificar contraseñas de usuarios.
     * Se inyecta automáticamente por Spring.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Verifica credenciales de acceso.
     * Busca por email y compara la contraseña cifrada.
     * 
     * @param email correo del usuario
     * @param password contraseña en texto plano
     * @return usuario válido o null si son inválidas
     */
    public User authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return null;
        }

        String storedHash = user.getPassword();
        if (storedHash == null || storedHash.isBlank()) {
            // Datos legacy: mejor tratar como credenciales inválidas que lanzar 500
            log.warn("Usuario {} tiene contraseña vacía/nula en BD", email);
            return null;
        }

        try {
            if (passwordEncoder.matches(password, storedHash)) {
                return user;
            }
        } catch (IllegalArgumentException ex) {
            // Ocurre si el hash no tiene formato BCrypt válido (ej. texto plano)
            log.warn("Hash de contraseña inválido para usuario {}: {}", email, ex.getMessage());
        }
        return null;
    }

    /**
     * Comprueba si un correo ya existe.
     * Útil para evitar registros duplicados.
     * 
     * @param email correo a verificar
     * @return true si existe, false si está disponible
     */
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Crea un usuario nuevo.
     * Cifra la contraseña antes de guardar.
     * 
     * @param registerRequest datos del usuario
     * @return usuario creado y persistido
     */
    public User createUser(User registerRequest) {

        // Codifica la contraseña antes de guardarla en la base de datos
        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // Guarda el nuevo usuario en la base de datos
        return userRepository.save(registerRequest);
    }
    
    /**
     * Busca usuarios por nombre.
     * No distingue mayúsculas y minúsculas y admite coincidencias parciales.
     * 
     * @param username nombre o fragmento a buscar
     * @return lista de usuarios coincidentes
     */
    public List<User> searchUsersByUsername(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username);
    }
    
    /**
     * Busca un usuario por su ID.
     * 
     * @param id identificador del usuario
     * @return Optional con el usuario o vacío si no existe
     */
    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    /**
     * Actualiza datos permitidos de un usuario.
     * Modifica nombre, apellido, dirección y cédula.
     * 
     * @param userId ID del usuario
     * @param updatedUserInfo nuevos datos
     * @return true si actualiza, false si no existe
     */
    public boolean updateUserInfo(UUID userId, User updatedUserInfo) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Solo actualizamos los campos permitidos
            user.setFirstName(updatedUserInfo.getFirstName());
            user.setLastName(updatedUserInfo.getLastName());
            user.setAddress(updatedUserInfo.getAddress());
            user.setCedula(updatedUserInfo.getCedula());

            userRepository.save(user);
            return true;
        }
        return false;
    }
}
