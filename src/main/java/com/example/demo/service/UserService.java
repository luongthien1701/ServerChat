package com.example.demo.service;

import com.example.demo.dto.noti.Locate;
import com.example.demo.dto.user.*;
import com.example.demo.model.Users;

import java.util.List;

public interface UserService {
    LoginResponse login(UserLoginRequest userLoginRequest);
    void register(UserRegisterRequest userRegisterRequest);
    List<UserResponse> findAllUsers();
    List<Users> findAll();
    Users findByUsername(String username);
    void save(UserRegisterRequest user);
    Users findByEmail(String email);
    Users findByDisplay_name(String display_name);
    UserSummary findById(Long id);
    List<Users> findAllByLikeDisplayName(String likeDisplayName);
    UserProfile findProfileById(Long id);
    String changePassword(Long userId, String oldPassword, String newPassword);
    void changeAvatar(Long userId, String avatar);
    void changeProfile(UserProfile userProfile);
    List<UserNearResponse> findUserNear(Long userId);
    void updateUserLocation(Long userId, Locate locate);
}
