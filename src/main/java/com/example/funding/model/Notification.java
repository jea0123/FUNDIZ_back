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
    private Long targetId;
    private String type;
    private String message;
    private Character isRead;
    private LocalDateTime createdAt;
}
