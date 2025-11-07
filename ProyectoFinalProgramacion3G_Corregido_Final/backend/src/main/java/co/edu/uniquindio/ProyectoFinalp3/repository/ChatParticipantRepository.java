/**
 * Repositorio de participantes de chat.
 * Administra relaciones usuario-chat.
 */
package co.edu.uniquindio.ProyectoFinalp3.repository;

import co.edu.uniquindio.ProyectoFinalp3.models.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, UUID> {

    // Encuentra todos los participantes de un chat específico
    List<ChatParticipant> findByChat_Id(UUID chatId);

    // Encuentra todos los chats en los que un usuario participa
    List<ChatParticipant> findByUser_Id(UUID userId);
}
