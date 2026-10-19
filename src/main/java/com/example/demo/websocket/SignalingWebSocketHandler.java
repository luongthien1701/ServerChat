package com.example.demo.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SignalingWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions =
            new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.put(session.getId(), session);

        System.out.println(
                "Client connected: " + session.getId()
        );
    }

    @Override
    protected void handleTextMessage(
            WebSocketSession session,
            TextMessage message
    ) throws Exception {

        String payload = message.getPayload();

        System.out.println(
                "Received from " + session.getId() + ": " + payload
        );

        for (WebSocketSession other : sessions.values()) {

            if (other.isOpen() &&
                    !other.getId().equals(session.getId())) {

                other.sendMessage(
                        new TextMessage(payload)
                );
            }
        }
    }

    @Override
    public void afterConnectionClosed(
            WebSocketSession session,
            org.springframework.web.socket.CloseStatus status
    ) {
        sessions.remove(session.getId());

        System.out.println(
                "Client disconnected: " + session.getId()
        );
    }
}