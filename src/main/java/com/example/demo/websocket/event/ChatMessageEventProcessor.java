package com.example.demo.websocket.event;

import com.example.demo.dto.chat.MessageDTO;
import com.example.demo.dto.chat.MessengerResponse;
import com.example.demo.manager.ChatSessionManager;
import com.example.demo.manager.GlobalOnlineManager;
import com.example.demo.service.MessageService;
import com.example.demo.service.ChatRoomService;
import com.example.demo.websocket.WebSocketMessageSender;
import com.example.demo.worker.DBWorker;
import com.example.demo.worker.SaveMessageTask;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;

/**
 * Xử lý sự kiện "message" từ WebSocket.
 * Tuân thủ SRP - chỉ lo việc chat message.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ChatMessageEventProcessor {

    private final MessageService messageService;
    private final ChatRoomService chatRoomService;
    private final WebSocketMessageSender messageSender;
    private final ObjectMapper objectMapper;
    private final DBWorker dbWorker;

    public void process(WebSocketSession session, Map<String, Object> payload) throws Exception {
        MessageDTO dto = objectMapper.convertValue(payload, MessageDTO.class);
        MessengerResponse response = new MessengerResponse(dto);

        Set<Long> members = chatRoomService.getMemberInRoom(dto.getChatRoomId());
        for (Long userId : members) {
            WebSocketSession userSession = GlobalOnlineManager.getSession(userId);
            if (userSession != null && userSession.isOpen()) {
                if (ChatSessionManager.isUserInRoom(userId, dto.getChatRoomId())) {
                    log.info("User {} is already in room", userId);
                    messageSender.sendEvent(userSession, "message", response);
                }
            }
        }
        dbWorker.submit(new SaveMessageTask(messageService, dto));
    }
}
