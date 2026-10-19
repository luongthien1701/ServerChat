package com.example.demo.service.impl;

import com.example.demo.dto.noti.AgoraTokenResponse;
import com.example.demo.service.AgoraService;
import io.agora.media.RtcTokenBuilder;
import org.springframework.stereotype.Service;

@Service
public class AgoraServiceImpl implements AgoraService {

    private static final String APP_ID = "c43cb3a3918a4d6f9ce96e5f3cbd5aab";
    private static final String APP_CERTIFICATE = "122489cbce4a48b4a2917b0495391ce9";

    @Override
    public AgoraTokenResponse generateToken(String channelName, int userId) throws Exception {
        int expirationTimeInSeconds = 3600;
        int currentTimeInSeconds = (int) (System.currentTimeMillis() / 1000);
        int privilegeExpirationTimeInSeconds = expirationTimeInSeconds + currentTimeInSeconds;

        RtcTokenBuilder tokenBuilder = new RtcTokenBuilder();
        String token = tokenBuilder.buildTokenWithUid(
                APP_ID,
                APP_CERTIFICATE,
                channelName,
                userId,
                RtcTokenBuilder.Role.Role_Publisher,
                privilegeExpirationTimeInSeconds
        );

        return new AgoraTokenResponse(token);
    }
}
