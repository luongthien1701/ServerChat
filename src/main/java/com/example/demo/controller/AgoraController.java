package com.example.demo.controller;

import com.example.demo.dto.noti.AgoraTokenResponse;
import com.example.demo.service.AgoraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agora")
@RequiredArgsConstructor
public class AgoraController {

    private final AgoraService agoraService;

    @GetMapping("/token")
    public ResponseEntity<AgoraTokenResponse> getToken(
            @RequestParam String channelName,
            @RequestParam int userId
    ) throws Exception {
        return ResponseEntity.ok(agoraService.generateToken(channelName, userId));
    }
}
