package com.omar.notification_server.notification;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Notification {
    private NotificationStatus status;
    private String message;
    private String bookTitle;
}
