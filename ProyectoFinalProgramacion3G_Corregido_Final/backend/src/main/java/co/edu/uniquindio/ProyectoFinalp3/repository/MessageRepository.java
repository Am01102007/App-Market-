/**
 * Repositorio de mensajes.
 * Ofrece consultas por chat y usuario.
 */
package co.edu.uniquindio.ProyectoFinalp3.repository;

import co.edu.uniquindio.ProyectoFinalp3.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    // Encuentra todos los mensajes de un chat específico
    List<Message> findByChat_Id(UUID chatId);
}
