package com.example.demo.controller;

import com.example.demo.dto.common.ApiResponse;
import com.example.demo.dto.friend.FriendRequest;
import com.example.demo.dto.friend.FriendResponse;
import com.example.demo.dto.friend.FriendStatusResponse;
import com.example.demo.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friend")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<FriendResponse>> getFriends(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(friendService.getAllFriends(userId));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addFriend(@RequestBody FriendRequest friendRequest) {
        friendService.addFriend(friendRequest);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse> removeFriend(@RequestBody FriendRequest friendRequest) {
        friendService.removeFriend(friendRequest);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PatchMapping("/block")
    public ResponseEntity<ApiResponse> blockFriend(@RequestBody FriendRequest friendRequest) {
        friendService.blockFriend(friendRequest);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PatchMapping
    public ResponseEntity<ApiResponse> acceptFriend(@RequestBody FriendRequest friendRequest) {
        friendService.acceptFriend(friendRequest.getSenderId(), friendRequest.getReceiverId());
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/{userId}/{friendId}")
    public ResponseEntity<FriendStatusResponse> checkFriend(
            @PathVariable("userId") Long userId,
            @PathVariable("friendId") Long friendId
    ) {
        return ResponseEntity.ok(new FriendStatusResponse(friendService.checkFriend(friendId, userId)));
    }
}
