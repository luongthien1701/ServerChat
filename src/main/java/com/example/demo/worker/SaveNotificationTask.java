package com.example.demo.worker;

import com.example.demo.dto.noti.NotificationDTO;
import com.example.demo.service.NotiService;

public class SaveNotificationTask implements DbTask {

    private final NotiService notiService;
    private final NotificationDTO notificationDTO;

    public SaveNotificationTask(NotiService notiService, NotificationDTO notificationDTO) {
        this.notiService = notiService;
        this.notificationDTO = notificationDTO;
    }

    @Override
    public void execute() {
        notiService.save(notificationDTO);
    }
}
