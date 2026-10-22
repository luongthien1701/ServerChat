package com.example.demo.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class CallManager {
    private static final ConcurrentHashMap<Long, Set<Long>> callMembers = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Long, Long> callers = new ConcurrentHashMap<>();

    public static void startCall(Long roomId, Long userId) {
        log.info("Start call for user {}", userId);
        if (callMembers.containsKey(roomId)) {
            return;
        }
        callMembers.putIfAbsent(roomId, ConcurrentHashMap.newKeySet());
        callMembers.get(roomId).add(userId);
        callers.put(roomId, userId);
    }

    public static void joinCall(Long roomId, Long userId) {
        callMembers.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(userId);
    }

    public static void leaveCall(Long roomId, Long userId) {
        Set<Long> set = callMembers.get(roomId);
        if (set == null) {
            return;
        }
        set.remove(userId);
        if (set.isEmpty()) {
            callMembers.remove(roomId);
            callers.remove(roomId);
        }
    }

    public static Set<Long> getCallMembers(Long roomId) {
        Set<Long> members = callMembers.get(roomId);
        log.info("Get members for user {}", members);
        return members;
    }

    public static Long getCallerId(Long roomId) {
        Long callerId = callers.get(roomId);
        return callerId;
    }
    public static void removeUserFromAllCalls(Long userId) {

        callMembers.forEach((roomId, members) -> {
            if (members.contains(userId)) {
                members.remove(userId);

                // Nếu room trống → xóa room + caller
                if (members.isEmpty()) {
                    callMembers.remove(roomId);
                    callers.remove(roomId);
                }
            }
        });
    }

}

