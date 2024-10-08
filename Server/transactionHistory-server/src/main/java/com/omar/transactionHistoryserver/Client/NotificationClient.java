package com.omar.transactionHistoryserver.Client;


import com.omar.transactionHistoryserver.config.FeignClientConfig;
import com.omar.transactionHistoryserver.dto.Notification;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-server",url = "${application.config.notification-server-url}",configuration = FeignClientConfig.class)
public interface NotificationClient {

    @PostMapping("/send/{userId}")
    void sendNotification(@PathVariable("userId") String userId, @RequestBody Notification notification);
}

