package com.example.demo.controller;

import com.example.demo.dto.noti.Locate;
import com.example.demo.dto.user.*;
import com.example.demo.manager.LocateManager;
import com.example.demo.model.Users;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/user")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    public List<Users> findAll() {
        System.out.println("api đc gọi");
        return userService.findAll();
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest userLoginRequest) {
        Users user=userService.findByUsername(userLoginRequest.getUsername());
        if(user==null){
            return ResponseEntity.badRequest().build();
        }
        if (user.getPassword().equals(userLoginRequest.getPassword())){
            return ResponseEntity.ok().body(Map.of("id",user.getId(),"name",user.getDisplayName(),"avatarUrl",user.getAvatarUrl()==null?"":user.getAvatarUrl()));
        }
        return ResponseEntity.unprocessableEntity().build();
    }
    @PostMapping()
    public ResponseEntity<?> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        Users _username=userService.findByUsername(userRegisterRequest.getUsername());
        Users _email=userService.findByEmail(userRegisterRequest.getEmail());
        Users _display_name=userService.findByDisplay_name(userRegisterRequest.getDisplay_name());

        if(_username!=null){
            return ResponseEntity.badRequest().body(Map.of("message","Tên đăng nhập đã tồn tại!Vui lòng thử lại"));
        }
        else
        if(_email!=null){
            return ResponseEntity.badRequest().body(Map.of("message","Email đã tồn tại!Vui lòng thử lại"));
        }
        else if(_display_name!=null){
                return ResponseEntity.badRequest().body(Map.of("message","Tên hiển thị đã tồn tại!Vui lòng thử lại"));
            }
        else
        {
            userService.save(userRegisterRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message","Tài khoản đăng kí thành công!!!"));

        }
    }
    @GetMapping("/{userId}")
    public UserSummary findById(@PathVariable("userId") Long userId) {
        return userService.findById(userId);
    }
    @GetMapping("/profile/{userId}")
    public UserProfile findProfileById(@PathVariable("userId") Long userId) {
        return userService.findProfileById(userId);
    }
    @PutMapping("/changepassword")
    public String changepassword(@RequestBody ChangePassword changePassword) {
        return userService.changePassword(changePassword.getId(),changePassword.getOldPassword(),changePassword.getNewPassword());
    }
    @PutMapping("/changeavatar")
    public ResponseEntity<?> changeAvatar(@RequestBody UserAvatarUpdateRequest updateAvatar) {
        userService.changeAvatar((long) updateAvatar.getId(),updateAvatar.getUrl());
        return  ResponseEntity.ok().body(Map.of("message","success"));
    }
    @PutMapping("/changeprofile")
    public ResponseEntity<?> changeProfile(@RequestBody UserProfile userProfile) {
        userService.changeProfile(userProfile);
        return  ResponseEntity.ok().body(Map.of("message","success"));
    }
    @GetMapping("/find")
    public ResponseEntity<?> findUserNear(@RequestParam Long userId) {
        return ResponseEntity.ok().body(userService.findUserNear(userId));
    }
    @PostMapping("/changestate/{userId}")
    public ResponseEntity<?> addUserLocate(@PathVariable Long userId,@RequestBody Locate locate)
    {
        LocateManager.addLocate(userId,locate.getLat(),locate.getLon());
        return ResponseEntity.ok().body(Map.of("message","success"));
    }
}
