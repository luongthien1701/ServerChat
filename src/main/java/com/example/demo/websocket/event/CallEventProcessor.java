package com.example.demo.websocket.event;

import com.example.demo.manager.CallManager;
import com.example.demo.manager.GlobalOnlineManager;
import com.example.demo.service.ChatMemberService;
import com.example.demo.websocket.WebSocketMessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Xử lý tất cả các sự kiện liên quan đến cuộc gọi (call_invite, call_accept, call_reject, call_end).
 * Tuân thủ SRP - chỉ lo việc quản lý cuộc gọi.
 */
@Component
@RequiredArgsConstructor
public class CallEventProcessor {

    private final ChatMemberService chatMemberService;
    private final WebSocketMessageSender messageSender;

    public void processCallInvite(WebSocketSession session, Map<String, Object> payload) throws Exception {
        Long roomId = ((Number) payload.get("roomId")).longValue();
        Long userId = ((Number) payload.get("userId")).longValue();
        CallManager.startCall(roomId, userId);

        List<Long> members = chatMemberService.getMembers(roomId);
        for (Long memberId : members) {
            if (memberId.equals(userId)) continue;
            if (GlobalOnlineManager.isOnlineUser(memberId)) {
                WebSocketSession userSession = GlobalOnlineManager.getSession(memberId);
                messageSender.sendEvent(userSession, "call_comming", payload);
            }
        }
    }

    public void processCallAccept(WebSocketSession session, Map<String, Object> payload) throws Exception {
        Long roomId = ((Number) payload.get("roomId")).longValue();
        Long userId = ((Number) payload.get("userId")).longValue();
        CallManager.joinCall(roomId, userId);

        Long callerId = CallManager.getCallerId(roomId);
        if (GlobalOnlineManager.isOnlineUser(callerId)) {
            WebSocketSession userSession = GlobalOnlineManager.getSession(callerId);
            messageSender.sendEvent(userSession, "call_accept", payload);
        }
    }

    public void processCallReject(WebSocketSession session, Map<String, Object> payload) throws Exception {
        Long userId = ((Number) payload.get("userId")).longValue();
        Long roomId = ((Number) payload.get("roomId")).longValue();
        Set<Long> members = CallManager.getCallMembers(roomId);
        for (Long memberId : members) {
            if (memberId.equals(userId)) continue;
            if (GlobalOnlineManager.isOnlineUser(memberId)) {
                WebSocketSession userSession = GlobalOnlineManager.getSession(memberId);
                messageSender.sendEvent(userSession, "call_reject", payload);
            }
        }
    }

    public void processCallEnd(WebSocketSession session, Map<String, Object> payload) throws Exception {
        Long roomId = ((Number) payload.get("roomId")).longValue();
        Long userId = ((Number) payload.get("userId")).longValue();
        Set<Long> members = CallManager.getCallMembers(roomId);
        for (Long memberId : members) {
            if (memberId.equals(userId)) continue;
            if (GlobalOnlineManager.isOnlineUser(memberId)) {
                WebSocketSession userSession = GlobalOnlineManager.getSession(memberId);
                messageSender.sendEvent(userSession, "call_end", payload);
            }
        }
    }
}
