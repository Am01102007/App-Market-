/**
 * Modelo de chat.
 * Contiene mensajes y participantes asociados.
 */
package co.edu.uniquindio.ProyectoFinalp3.models;

import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "chats")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID id;

    private String name; // Nombre del chat (opcional, útil para chats grupales)

    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "chat")
    private List<ChatParticipant> chatParticipants;

    // Constructor vacío
    public Chat() {
        this.createdAt = LocalDateTime.now(); // Se inicializa la fecha de creación
    }

    // Constructor con parámetros
    public Chat(String name) {
        this.name = name;
        this.createdAt = LocalDateTime.now();
    }

    // Getters y Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
