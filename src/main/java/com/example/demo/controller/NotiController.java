package com.example.demo.controller;

import com.example.demo.dto.common.ApiResponse;
import com.example.demo.dto.noti.NotificationDTO;
import com.example.demo.dto.noti.TokenNotiRequest;
import com.example.demo.dto.noti.TokenResponse;
import com.example.demo.service.NotiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotiController {

    private final NotiService notiService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationDTO>> getNoti(@PathVariable Long userId) {
        return ResponseEntity.ok(notiService.findAll(userId));
    }

    @GetMapping("/token")
    public ResponseEntity<TokenResponse> getToken(@RequestParam Long userId) {
        return ResponseEntity.ok(new TokenResponse(notiService.getToken(userId)));
    }

    @PostMapping("/token")
    public ResponseEntity<ApiResponse> updateToken(@RequestBody TokenNotiRequest tokenNotiRequest) {
        notiService.setToken(tokenNotiRequest.getUserId(), tokenNotiRequest.getToken());
        return ResponseEntity.ok(ApiResponse.success());
    }
}
