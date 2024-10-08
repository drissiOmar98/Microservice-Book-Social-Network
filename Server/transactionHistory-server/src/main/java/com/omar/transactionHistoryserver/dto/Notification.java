package com.omar.transactionHistoryserver.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    private NotificationStatus status;
    private String message;
    private String bookTitle;
}
