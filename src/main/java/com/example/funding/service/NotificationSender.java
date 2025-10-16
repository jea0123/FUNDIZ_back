package com.example.funding.service;

import com.example.funding.common.NotificationSseHub;
import com.example.funding.mapper.NotificationMapper;
import com.example.funding.model.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationSender {
    private final NotificationMapper notificationMapper;
    private final NotificationSseHub hub;

    /**
     * 알림을 생성하고 해당 유저에게 실시간으로 전송.
     * @param userId 알림을 받을 유저의 ID
     * @param message 알림 메시지
     * @param type 알림 유형
     * @param targetId 알림과 관련된 대상의 ID (예: 댓글 ID, 게시글 ID 등)
     */
    public void createAndSend(Long userId, String message, String type, Long targetId){
        Notification noti = Notification.builder()
                .userId(userId)
                .type(type)
                .targetId(targetId)
                .message(message)
                .isRead('N')
                .build();
        notificationMapper.insertNotification(noti);
        Long notificationId = noti.getNotificationId();
        Notification notification = notificationMapper.getNotificationById(notificationId);
        hub.sendToUser(notification);
    }
}
