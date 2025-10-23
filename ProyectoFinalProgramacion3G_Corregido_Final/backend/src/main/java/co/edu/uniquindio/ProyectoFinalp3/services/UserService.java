package co.edu.uniquindio.ProyectoFinalp3.services;

import co.edu.uniquindio.ProyectoFinalp3.dto.UpdateUserRequest;
import co.edu.uniquindio.ProyectoFinalp3.models.User;
import co.edu.uniquindio.ProyectoFinalp3.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio para manejar todas las operaciones relacionadas con usuarios.
 * Proporciona funcionalidades para autenticación, registro, búsqueda y actualización de usuarios.
 * Utiliza encriptación de contraseñas para garantizar la seguridad.
 * 
 * @author Sistema App Market
 * @version 1.0
 */
@Service
public class UserService {

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
     * Autentica a un usuario verificando sus credenciales.
     * Busca al usuario por email y verifica que la contraseña coincida con la encriptada.
     * 
     * @param email Email del usuario que intenta autenticarse
     * @param password Contraseña en texto plano proporcionada por el usuario
     * @return User si las credenciales son correctas, null si son incorrectas
     */
    public User authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    /**
     * Verifica si un email ya está registrado en el sistema.
     * Útil para evitar duplicados durante el registro de nuevos usuarios.
     * 
     * @param email Email a verificar
     * @return true si el email ya existe, false si está disponible
     */
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Crea un nuevo usuario en el sistema.
     * Encripta la contraseña antes de guardarla en la base de datos.
     * 
     * @param registerRequest Objeto User con los datos del nuevo usuario
     * @return User creado y guardado en la base de datos
     */
    public User createUser(User registerRequest) {

        // Codifica la contraseña antes de guardarla en la base de datos
        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // Guarda el nuevo usuario en la base de datos
        return userRepository.save(registerRequest);
    }
    
    /**
     * Busca usuarios por su nombre de usuario.
     * Realiza una búsqueda que no distingue entre mayúsculas y minúsculas
     * y puede encontrar coincidencias parciales.
     * 
     * @param username Nombre de usuario o parte del nombre a buscar
     * @return Lista de usuarios que coinciden con el criterio de búsqueda
     */
    public List<User> searchUsersByUsername(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username);
    }
    
    /**
     * Busca un usuario específico por su ID único.
     * 
     * @param id UUID del usuario a buscar
     * @return Optional<User> que contiene el usuario si existe, vacío si no existe
     */
    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    /**
     * Actualiza la información personal de un usuario autenticado.
     * Solo permite actualizar campos específicos como nombre, apellido, dirección y cédula.
     * No permite cambiar email, contraseña o rol por seguridad.
     * 
     * @param userId ID del usuario a actualizar
     * @param updatedUserInfo Objeto User con la nueva información
     * @return true si la actualización fue exitosa, false si el usuario no existe
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