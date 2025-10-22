package co.edu.uniquindio.ProyectoFinalp3.repository;

import co.edu.uniquindio.ProyectoFinalp3.models.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // Aquí se pueden definir consultas personalizadas si es necesario
    User findByEmail(String email); 
    
    // Verificar si el correo electrónico ya existe
    boolean existsByEmail(String email);
    // Método para buscar un usuario por su username
    Optional<User> findByUsername(String username);
    // Método para buscar usuarios por parte del nombre de usuario
    List<User> findByUsernameContainingIgnoreCase(String username);
    public Optional<User> getUserById(UUID id);
    

}