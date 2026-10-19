package com.example.demo.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;

/**
 * Helper component chịu trách nhiệm duy nhất là gửi message qua WebSocket.
 * Tuân thủ SRP - chỉ làm một việc: serialize và gửi message.
 */
@Component
@RequiredArgsConstructor
public class WebSocketMessageSender {

    private final ObjectMapper objectMapper;

    /**
     * Gửi event có data tới một session cụ thể.
     */
    public void sendEvent(WebSocketSession session, String event, Object data) throws IOException {
        if (session == null || !session.isOpen()) return;
        Map<String, Object> wrapper = Map.of("event", event, "data", data);
        String json = objectMapper.writeValueAsString(wrapper);
        synchronized (session) {
            session.sendMessage(new TextMessage(json));
        }
    }

    /**
     * Gửi raw payload tới một session.
     */
    public void sendRaw(WebSocketSession session, Object payload) throws IOException {
        if (session == null || !session.isOpen()) return;
        String json = objectMapper.writeValueAsString(payload);
        synchronized (session) {
            session.sendMessage(new TextMessage(json));
        }
    }
}
