package com.example.demo.handler;

import com.example.demo.manager.CallManager;
import com.example.demo.manager.GlobalOnlineManager;
import com.example.demo.manager.LocateManager;
import com.example.demo.websocket.event.CallEventProcessor;
import com.example.demo.websocket.event.ChatMessageEventProcessor;
import com.example.demo.websocket.event.NotificationEventProcessor;
import com.example.demo.websocket.event.RoomSessionEventProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;

/**
 * ChatHandler chịu trách nhiệm duy nhất: parse event và điều hướng sang processor tương ứng.
 * Tuân thủ SRP - không chứa business logic.
 */
@AllArgsConstructor
@Component
@Slf4j
public class ChatHandler extends TextWebSocketHandler {

    private final ChatMessageEventProcessor chatMessageProcessor;
    private final NotificationEventProcessor notificationProcessor;
    private final CallEventProcessor callEventProcessor;
    private final RoomSessionEventProcessor roomSessionProcessor;
    private final ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        try {
            Object userIdAttribute=session.getAttributes().get("userId");
            if (!(userIdAttribute instanceof Long userId))
            {
                session.close();
                return;
            }
            GlobalOnlineManager.addOnlineUser(userId, session);
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        try {
            Long userId = GlobalOnlineManager.getUserId(session);
            if (userId != null) {
                GlobalOnlineManager.removeOnlineUser(userId);
                CallManager.removeUserFromAllCalls(userId);
                LocateManager.removeLocate(userId);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        try {
            Map<String, Object> map = objectMapper.readValue(textMessage.getPayload(), Map.class);
            String event = (String) map.get("event");
            Map<String, Object> data = (Map<String, Object>) map.get("data");


            switch (event) {
                case "call_invite"     -> callEventProcessor.processCallInvite(session, data);
                case "call_accept"     -> callEventProcessor.processCallAccept(session, data);
                case "call_reject"     -> callEventProcessor.processCallReject(session, data);
                case "call_end"        -> callEventProcessor.processCallEnd(session, data);
                case "message"         -> chatMessageProcessor.process(session, data);
                case "notification"    -> notificationProcessor.process(session, data);
                case "join_room"       -> roomSessionProcessor.processJoinRoom(data);
                case "leave_room"      -> roomSessionProcessor.processLeaveRoom(data);
                default -> log.info(event);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
    }
}
