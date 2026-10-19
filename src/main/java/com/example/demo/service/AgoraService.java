package com.example.demo.service;

import com.example.demo.dto.noti.AgoraTokenResponse;

public interface AgoraService {
    AgoraTokenResponse generateToken(String channelName, int userId) throws Exception;
}
