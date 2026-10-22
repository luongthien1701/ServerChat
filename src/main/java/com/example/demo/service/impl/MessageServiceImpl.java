package com.example.demo.service.impl;

import com.example.demo.dto.chat.MessageDTO;
import com.example.demo.dto.chat.MessengerResponse;
import com.example.demo.model.ChatRoom;
import com.example.demo.model.Message;
import com.example.demo.model.Users;
import com.example.demo.repository.ChatRoomRepository;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.UsersRepository;
import com.example.demo.service.MessageService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final UsersRepository usersRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public List<MessengerResponse> findAllByChatRoom_Id(Long chatRoomId) {
        return messageRepository.findAllByChatRoom_Id(chatRoomId);
    }

    @Override
    public List<MessengerResponse> findLikeContent(Long userId, String content) {
        return messageRepository.findMessageInChatroom(userId, content);
    }

    @Override
    @Transactional
    public Message save(MessageDTO message) {

        Users user = usersRepository.findById(message.getSenderId()).orElse(null);
        ChatRoom chatRoom = chatRoomRepository.findById(message.getChatRoomId()).orElse(null);
        if (chatRoom != null && user != null) {
            Message messageEntity = new Message();
            messageEntity.setChatRoom(chatRoom);
            messageEntity.setSender(user);
            messageEntity.setContent(message.getContent());
            if (message.getFileUrl() != null) {
                messageEntity.setFileUrl(message.getFileUrl());
                messageEntity.setType(Message.MessageType.IMAGE);
            } else {
                messageEntity.setType(Message.MessageType.TEXT);
            }
            messageRepository.save(messageEntity);
            chatRoom.setLastMessageId(messageEntity.getId());
            chatRoom.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            chatRoomRepository.save(chatRoom);
            return messageEntity;
        } else {
            throw new IllegalStateException("Lỗi phòng");
        }
    }
}
