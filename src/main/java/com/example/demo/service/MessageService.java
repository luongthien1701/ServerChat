package com.example.demo.service;

import com.example.demo.dto.chat.MessageDTO;
import com.example.demo.dto.chat.MessengerResponse;
import com.example.demo.model.Message;

import java.util.List;

public interface MessageService {
    List<MessengerResponse> findAllByChatRoom_Id(Long chatRoomId);
    List<MessengerResponse> findLikeContent(Long userId, String content);
    Message save(MessageDTO message);
}
