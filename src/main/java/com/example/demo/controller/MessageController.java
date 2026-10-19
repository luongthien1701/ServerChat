package com.example.demo.controller;

import com.example.demo.dto.chat.MessengerResponse;
import com.example.demo.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messager")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/{roomId}")
    public ResponseEntity<List<MessengerResponse>> getMessageByRoomId(@PathVariable("roomId") Long roomId) {
        return ResponseEntity.ok(messageService.findAllByChatRoom_Id(roomId));
    }
}
