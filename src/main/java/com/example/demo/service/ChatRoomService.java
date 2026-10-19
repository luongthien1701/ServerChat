package com.example.demo.service;

import com.example.demo.dto.chat.ChatRoomDTO;
import com.example.demo.dto.chat.RoomRequest;
import com.example.demo.model.ChatRoom;

import java.util.List;
import java.util.Set;

public interface ChatRoomService {
    List<ChatRoomDTO> GetallRoomById(Long id);
    List<ChatRoom> findbylikeName(Long userId, String like);
    ChatRoomDTO findbyConvervationId(Long convervationId);
    Long findRoom(Long userId, Long friendId);
    Set<Long> getMemberInRoom(Long roomId);
    void addMember(Long roomId, Long userId);
    void changeRoom(Long roomId, String type, String body);
    String updateRoomSession(RoomRequest chatRoom);
}
