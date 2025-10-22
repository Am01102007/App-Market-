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

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Autenticación de usuario: Verifica si las credenciales son correctas
    public User authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    // Verifica si un correo electrónico ya existe en la base de datos
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    // Crea un nuevo usuario
    public User createUser(User registerRequest) {

        // Codifica la contraseña antes de guardarla en la base de datos
        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // Guarda el nuevo usuario en la base de datos
        return userRepository.save(registerRequest);
    }
    // Método para buscar usuarios por su username (o parte del mismo)
    public List<User> searchUsersByUsername(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username);
    }
     // Nuevo método para buscar usuario por ID
    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

   

    // Actualiza la información del usuario autenticado
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