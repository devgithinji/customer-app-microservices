package com.densoft.clients.notifications;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "notification", url = "${clients.notification.url}/api/v1/notification")
public interface NotificationClient {
    @PostMapping
    void sendNotification(NotificationRequest notificationRequest);
}
