package com.example.funding.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    private Long notificationId;
    private Long userId;
    private String type;
    private Long targetId;
    private String message;
    private char isRead;
    private LocalDateTime createdAt;
}
