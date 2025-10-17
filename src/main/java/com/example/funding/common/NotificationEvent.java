package com.example.funding.common;

import com.example.funding.enums.NotificationType;

public record NotificationEvent(
        Long userId,
        NotificationType type,
        String title,
        Long targetId,
        String idemKey
) {
    public static String defaultIdemKey(Long userId, NotificationType type, Long targetId) {
        return (userId == null ? "-" : userId) + "|" + type.name() + "|" + (targetId == null ? "-" : targetId);
    }
}
