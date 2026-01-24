package com.example.demo.repository;

import com.example.demo.dto.chat.MessageDTO;
import com.example.demo.dto.chat.MessengerResponse;
import com.example.demo.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("""
                SELECT new com.example.demo.dto.chat.MessengerResponse(
                    mess.id,
                    mess.content,
                    mess.fileUrl,
                    mess.type,
                    mess.status,
                    mess.sentAt,
                    mess.editedAt,
                    new com.example.demo.dto.user.UserSummary(
                        mess.sender.id,
                        null,
                        null
                    ),
                    mess.chatRoom.id,
                    mess.chatRoom.name
                )
                FROM Message mess
                WHERE mess.chatRoom.id = :chatRoomId
            """)
    List<MessengerResponse> findAllByChatRoom_Id(@Param("chatRoomId") Long chatRoomId);


    @Query("""
                SELECT new com.example.demo.dto.chat.MessengerResponse(
                    mess.id,
                    mess.content,
                    mess.fileUrl,
                    mess.type,
                    mess.status,
                    mess.sentAt,
                    mess.editedAt,
                    new com.example.demo.dto.user.UserSummary(
                        mess.sender.id,
                        null,
                        null
                    ),
                    mess.chatRoom.id,
                    mess.chatRoom.name
                )
                FROM Message mess
                JOIN mess.chatRoom
                WHERE mess.sender.id = :userId
                  AND mess.content LIKE %:like%
            """)
    List<MessengerResponse> findMessageInChatroom(
            @Param("userId") Long userId,
            @Param("like") String like
    );


    Message save(Message message);
}
