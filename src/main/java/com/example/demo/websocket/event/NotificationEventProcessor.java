package com.example.demo.websocket.event;

import com.example.demo.dto.noti.NotificationDTO;
import com.example.demo.manager.GlobalOnlineManager;
import com.example.demo.service.FCMService;
import com.example.demo.service.NewsfeedService;
import com.example.demo.service.NotiService;
import com.example.demo.service.UserService;
import com.example.demo.websocket.WebSocketMessageSender;
import com.example.demo.worker.DBWorker;
import com.example.demo.worker.SaveNotificationTask;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

/**
 * Xử lý sự kiện "notification" từ WebSocket.
 * Tuân thủ SRP - chỉ lo việc thông báo.
 */
@Component
@RequiredArgsConstructor
public class NotificationEventProcessor {

    private final NotiService notiService;
    private final UserService userService;
    private final NewsfeedService newsfeedService;
    private final FCMService fcmService;
    private final WebSocketMessageSender messageSender;
    private final ObjectMapper objectMapper;
    private final DBWorker dbWorker;

    public void process(WebSocketSession session, Map<String, Object> payload) throws Exception {
        NotificationDTO notification = objectMapper.convertValue(payload, NotificationDTO.class);

        if (GlobalOnlineManager.isOnlineUser(notification.getReceiver())) {
            WebSocketSession userSession = GlobalOnlineManager.getSession(notification.getReceiver());
            messageSender.sendEvent(userSession, "notification", notification);
        } else {
            sendPushNotification(notification);
        }

        dbWorker.submit(new SaveNotificationTask(notiService, notification));
    }

    private void sendPushNotification(NotificationDTO notification) throws Exception {
        String name = userService.findById(notification.getSender().getId()).getName();
        String token;
        Long userId;
        String type = notification.getType();

        switch (type) {
            case "message":
                token = notiService.getToken(notification.getReceiver());
                fcmService.sendMessage(token, name + " đã gửi cho bạn một tin nhắn");
                break;
            case "add_friend":
                token = notiService.getToken(notification.getReceiver());
                fcmService.sendMessage(token, name + " đã gửi lời mời kết bạn");
                break;
            case "accept_friend":
                token = notiService.getToken(notification.getReceiver());
                fcmService.sendMessage(token, name + " đã chấp nhận lời mời kết bạn");
                break;
            case "like_post":
                userId = newsfeedService.getUserId(notification.getReceiver());
                token = notiService.getToken(userId);
                fcmService.sendMessage(token, name + " đã thích bài viết của bạn");
                break;
            case "comment":
                userId = newsfeedService.getUserId(notification.getReceiver());
                token = notiService.getToken(userId);
                fcmService.sendMessage(token, name + " đã bình luận bài viết của bạn");
                break;
            default:
                break;
        }
    }
}
