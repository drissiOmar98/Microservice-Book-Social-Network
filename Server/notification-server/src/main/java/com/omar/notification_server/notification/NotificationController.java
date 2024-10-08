package com.omar.notification_server.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/send/{userId}")
    public ResponseEntity<Void> sendNotification(
            @PathVariable String userId,
            @RequestBody Notification notification) {
        notificationService.sendNotification(userId, notification);
        return ResponseEntity.ok().build();
    }
}
