/**
 * Servicio WebSocket.
 * Orquesta eventos y entrega de mensajes en tiempo real.
 */
package co.edu.uniquindio.ProyectoFinalp3.services;

import co.edu.uniquindio.ProyectoFinalp3.models.Message;
import co.edu.uniquindio.ProyectoFinalp3.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class WebSocketService {

    private final MessageRepository messageRepository;

    // Mapa para mantener las conexiones de los usuarios
    private final Map<String, WebSocketSession> userSessions = new HashMap<>();

    @Autowired
    public WebSocketService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // Método para agregar una sesión de WebSocket de un usuario
    public void addUserSession(String userId, WebSocketSession session) {
        userSessions.put(userId, session);
    }

    // Método para eliminar una sesión de WebSocket de un usuario
    public void removeUserSession(String userId) {
        userSessions.remove(userId);
    }

    // Enviar mensaje a un usuario
    public void sendMessageToUser(String userId, Message message) {
        WebSocketSession session = userSessions.get(userId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message.getContent()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para guardar el mensaje y luego enviar a los participantes
    public void saveAndSendMessage(Message message, String chatId) {
        messageRepository.save(message);
        // Aquí puedes usar un servicio para enviar el mensaje a todos los participantes
        // de un chat usando WebSocket
    }
}
