package com.example.demo.dto.chat;

import com.example.demo.model.Message;
import lombok.Data;

@Data
public class MessageDTO {
    private Long chatRoomId;
    private Long senderId;
    private String content;
    private String fileUrl;
    private Message.MessageType type;

}
