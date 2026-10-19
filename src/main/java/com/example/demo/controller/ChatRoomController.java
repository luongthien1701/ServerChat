package com.example.demo.controller;

import com.example.demo.dto.chat.ChatRoomDTO;
import com.example.demo.dto.chat.RoomIdResponse;
import com.example.demo.dto.chat.RoomRequest;
import com.example.demo.dto.common.ApiResponse;
import com.example.demo.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/room")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<ChatRoomDTO>> getAllrooms(@PathVariable Long userId) {
        return ResponseEntity.ok(chatRoomService.GetallRoomById(userId));
    }

    @GetMapping("/infomation/{convervationId}")
    public ResponseEntity<ChatRoomDTO> getinfomation(@PathVariable Long convervationId) {
        return ResponseEntity.ok(chatRoomService.findbyConvervationId(convervationId));
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateRoom(@RequestBody RoomRequest chatRoom) {
        return ResponseEntity.ok(chatRoomService.updateRoomSession(chatRoom));
    }

    @GetMapping("/find/{userId}/{friendId}")
    public ResponseEntity<RoomIdResponse> findroom(@PathVariable Long userId, @PathVariable Long friendId) {
        return ResponseEntity.ok(new RoomIdResponse(chatRoomService.findRoom(userId, friendId)));
    }

    @GetMapping("/users/{conversationId}")
    public ResponseEntity<Set<Long>> getAllUserByConversationId(@PathVariable Long conversationId) {
        return ResponseEntity.ok(chatRoomService.getMemberInRoom(conversationId));
    }

    @PostMapping("/add/{roomId}/{userId}")
    public ResponseEntity<ApiResponse> addUserToRoom(@PathVariable Long roomId, @PathVariable Long userId) {
        chatRoomService.addMember(roomId, userId);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
