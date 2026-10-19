package com.example.demo.service;

import com.example.demo.dto.noti.NotificationDTO;

import java.util.List;

public interface NotiService {
    List<NotificationDTO> findAll(Long userId);
    void save(NotificationDTO notificationDTO);
    String getToken(Long userId);
    void setToken(Long userId, String token);
}
