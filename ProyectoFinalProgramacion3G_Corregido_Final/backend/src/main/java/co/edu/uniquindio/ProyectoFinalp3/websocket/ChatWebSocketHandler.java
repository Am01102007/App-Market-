/**
 * Manejador WebSocket de chat.
 * Administra sesiones y distribución de mensajes.
 */
package co.edu.uniquindio.ProyectoFinalp3.websocket;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    // Este método maneja los mensajes de texto recibidos por el WebSocket
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Procesar mensaje recibido
        System.out.println("Mensaje recibido: " + message.getPayload());

        // Responder al cliente
        session.sendMessage(new TextMessage("Hola desde el servidor: " + message.getPayload()));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Manejar una nueva conexión
        System.out.println("Conexión establecida con id: " + session.getId());
    }
}
