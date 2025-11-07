/**
 * Repositorio de contactos.
 * Permite operaciones CRUD y búsquedas por usuario.
 */
package co.edu.uniquindio.ProyectoFinalp3.repository;

import co.edu.uniquindio.ProyectoFinalp3.models.Contact;
import co.edu.uniquindio.ProyectoFinalp3.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
@Repository
public interface ContactRepository extends JpaRepository<Contact, UUID> {

    // Encuentra todos los contactos de un usuario específico
    List<Contact> findByUser(User user);

    // Encuentra si ya existe una relación de contacto entre dos usuarios
    Optional<Contact> findByUserAndContactUser(User user, User contactUser);

    // Elimina un contacto específico entre dos usuarios
    void delete(Contact contact);
    // Método para encontrar sugerencias de contactos basadas en amigos mutuos
    @Query("SELECT DISTINCT c.contactUser FROM Contact c WHERE c.user IN " +
    "(SELECT c2.contactUser FROM Contact c2 WHERE c2.user = :user) " +
    "AND c.contactUser NOT IN (SELECT c3.contactUser FROM Contact c3 WHERE c3.user = :user)")
    List<User> findSuggestedContactsByMutualFriends(@Param("user") User user);

    
}
