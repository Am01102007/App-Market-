/**
 * Repositorio de usuarios.
 * Ofrece consultas por correo y nombre.
 */
package co.edu.uniquindio.ProyectoFinalp3.repository;

import co.edu.uniquindio.ProyectoFinalp3.models.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para manejar operaciones de base de datos relacionadas con usuarios.
 * Extiende JpaRepository para proporcionar operaciones CRUD básicas
 * y define métodos personalizados para consultas específicas de usuarios.
 * 
 * @author Sistema App Market
 * @version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Busca un usuario por su dirección de correo electrónico.
     * Utilizado principalmente para autenticación y verificación de duplicados.
     * 
     * @param email Dirección de correo electrónico del usuario
     * @return User si existe un usuario con ese email, null si no existe
     */
    User findByEmail(String email); 
    
    /**
     * Verifica si existe un usuario con el correo electrónico especificado.
     * Útil para validaciones durante el registro de nuevos usuarios.
     * 
     * @param email Dirección de correo electrónico a verificar
     * @return true si el email ya está registrado, false si está disponible
     */
    boolean existsByEmail(String email);
    
    /**
     * Busca un usuario por su nombre de usuario.
     * Devuelve un Optional para manejar casos donde el usuario no existe.
     * 
     * @param username Nombre de usuario a buscar
     * @return Optional<User> que contiene el usuario si existe, vacío si no existe
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Busca usuarios cuyo nombre de usuario contenga la cadena especificada.
     * La búsqueda no distingue entre mayúsculas y minúsculas.
     * Útil para funcionalidades de búsqueda y autocompletado.
     * 
     * @param username Parte del nombre de usuario a buscar
     * @return Lista de usuarios que coinciden con el criterio de búsqueda
     */
    List<User> findByUsernameContainingIgnoreCase(String username);
    
    /**
     * Busca un usuario por su ID único.
     * Método personalizado que complementa el findById heredado de JpaRepository.
     * 
     * @param id UUID del usuario a buscar
     * @return Optional<User> que contiene el usuario si existe, vacío si no existe
     */
    public Optional<User> getUserById(UUID id);
}
