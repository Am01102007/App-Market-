package co.edu.uniquindio.ProyectoFinalp3.models;

import java.util.UUID;
import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "chat_participants")
public class ChatParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat; // Relación con el chat

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Relación con el usuario

    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    // Constructor vacío
    public ChatParticipant() {
        this.joinedAt = LocalDateTime.now();
    }

    // Constructor con parámetros
    public ChatParticipant(Chat chat, User user) {
        this.chat = chat;
        this.user = user;
        this.joinedAt = LocalDateTime.now();
    }

    // Getters y Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }
}
