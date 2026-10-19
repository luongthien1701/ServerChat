package com.example.demo.service;

import com.google.firebase.messaging.FirebaseMessagingException;

public interface FCMService {
    String sendMessage(String token, String content) throws FirebaseMessagingException;
}
