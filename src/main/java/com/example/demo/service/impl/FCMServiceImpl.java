package com.example.demo.service.impl;

import com.example.demo.service.FCMService;
import com.google.firebase.messaging.*;
import org.springframework.stereotype.Service;

@Service
public class FCMServiceImpl implements FCMService {

    @Override
    public String sendMessage(String token, String content) throws FirebaseMessagingException {

        Message message = Message
                .builder()
                .setToken(token)
                .setNotification(
                        Notification.builder()
                                .setTitle("Thông báo")
                                .setBody(content)
                                .build()
                ).setAndroidConfig(AndroidConfig.builder().setPriority(AndroidConfig.Priority.HIGH).build())
                .build();
        return FirebaseMessaging.getInstance().send(message);
    }
}
