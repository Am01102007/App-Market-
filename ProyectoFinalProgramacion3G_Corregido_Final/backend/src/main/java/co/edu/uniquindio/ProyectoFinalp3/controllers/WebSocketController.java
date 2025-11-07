/**
 * Controlador WebSocket.
 * Gestiona conexiones y mensajes en tiempo real.
 */
package co.edu.uniquindio.ProyectoFinalp3.controllers;

import co.edu.uniquindio.ProyectoFinalp3.models.Message;
import co.edu.uniquindio.ProyectoFinalp3.services.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final WebSocketService webSocketService;

    @Autowired
    public WebSocketController(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    // Maneja los mensajes entrantes
    @MessageMapping("/chat/message")
    @SendTo("/topic/messages")
    public Message handleMessage(Message message) {
        // Aquí puedes realizar cualquier lógica adicional, como guardar el mensaje
        webSocketService.saveAndSendMessage(message, message.getChat().getId().toString());
        return message;
    }
}
