package com.example.demo.service.impl;

import com.example.demo.dto.noti.NotificationDTO;
import com.example.demo.model.Notification;
import com.example.demo.model.Notification_Token;
import com.example.demo.model.Users;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.NotificationTokenRepository;
import com.example.demo.repository.UsersRepository;
import com.example.demo.service.NotiService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class NotiServiceImpl implements NotiService {
    private final NotificationRepository repo;
    private final UsersRepository usersRepository;
    private final NotificationTokenRepository notificationTokenRepository;

    @Override
    public List<NotificationDTO> findAll(Long userId) {
        return repo.findAllByReceiver(userId);
    }

    @Override
    public void save(NotificationDTO notificationDTO) {
        Notification notification = new Notification();
        notification.setStatus(false);
        notification.setSenderId(usersRepository.findUserById(notificationDTO.getSender().getId()));
        notification.setReceiverId(notificationDTO.getReceiver());
        notification.setTitle(parseTitle(notificationDTO.getType(), notificationDTO.getSender().getId()));
        notification.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        repo.save(notification);
    }

    private String parseTitle(String type, Long senderId) {
        String displayName = usersRepository.findById(senderId).get().getDisplayName();
        switch (type) {
            case "add_friend":
                return displayName + " đã gửi cho bạn lời mời kết bạn";
            case "accept_friend":
                return displayName + " đã chấp nhận lời mới kết bạn";
            case "like_post":
                return displayName + " đã thích bài viết của bạn";
            case "comment":
                return displayName + " đã bình luận bài viết của bạn";
            default:
                return "";
        }
    }

    @Override
    public String getToken(Long userId) {
        return notificationTokenRepository.findByUser_Id(userId).getToken();
    }

    @Override
    public void setToken(Long userId, String token) {
        Notification_Token notificationToken = notificationTokenRepository.findByUser_Id(userId);
        if (notificationToken == null) {
            notificationToken = new Notification_Token();
            Users user = usersRepository.findUserById(userId);
            notificationToken.setUser(user);
            notificationToken.setToken(token);
            notificationTokenRepository.save(notificationToken);
        } else {
            notificationToken.setToken(token);
            notificationTokenRepository.save(notificationToken);
        }
    }
}
