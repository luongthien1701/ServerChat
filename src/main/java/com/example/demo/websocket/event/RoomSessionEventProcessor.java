package com.example.demo.websocket.event;

import com.example.demo.manager.ChatSessionManager;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Xử lý sự kiện join_room và leave_room từ WebSocket.
 * Tuân thủ SRP - chỉ quản lý việc tham gia/rời phòng.
 */
@Component
public class RoomSessionEventProcessor {

    public void processJoinRoom(Map<String, Object> data) {
        Long userId = ((Number) data.get("userId")).longValue();
        Long roomId = ((Number) data.get("roomId")).longValue();
        ChatSessionManager.addUserToRoom(userId, roomId);
    }

    public void processLeaveRoom(Map<String, Object> data) {
        Long userId = ((Number) data.get("userId")).longValue();
        Long roomId = ((Number) data.get("roomId")).longValue();
        ChatSessionManager.removeUserFromRoom(userId, roomId);
    }
}
