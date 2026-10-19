package com.example.demo.controller;

import com.example.demo.dto.common.ApiResponse;
import com.example.demo.dto.noti.Locate;
import com.example.demo.dto.user.*;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
        System.out.println(userLoginRequest);
        return ResponseEntity.ok(userService.login(userLoginRequest));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        userService.register(userRegisterRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Tài khoản đăng kí thành công!!!"));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserSummary> findById(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.findById(userId));
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<UserProfile> findProfileById(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.findProfileById(userId));
    }

    @PutMapping("/changepassword")
    public ResponseEntity<ApiResponse> changePassword(@RequestBody ChangePassword changePassword) {
        String result = userService.changePassword(
                changePassword.getId(),
                changePassword.getOldPassword(),
                changePassword.getNewPassword()
        );
        return ResponseEntity.ok(new ApiResponse(true, result));
    }

    @PutMapping("/changeavatar")
    public ResponseEntity<ApiResponse> changeAvatar(@RequestBody UserAvatarUpdateRequest updateAvatar) {
        userService.changeAvatar((long) updateAvatar.getId(), updateAvatar.getUrl());
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PutMapping("/changeprofile")
    public ResponseEntity<ApiResponse> changeProfile(@RequestBody UserProfile userProfile) {
        userService.changeProfile(userProfile);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/find")
    public ResponseEntity<List<UserNearResponse>> findUserNear(@RequestParam Long userId) {
        return ResponseEntity.ok(userService.findUserNear(userId));
    }

    @PostMapping("/changestate/{userId}")
    public ResponseEntity<ApiResponse> addUserLocate(@PathVariable Long userId, @RequestBody Locate locate) {
        userService.updateUserLocation(userId, locate);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
