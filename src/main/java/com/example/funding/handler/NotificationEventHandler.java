package com.example.funding.handler;

import com.example.funding.common.NotificationEvent;
import com.example.funding.common.NotificationSseHub;
import com.example.funding.mapper.NotificationMapper;
import com.example.funding.model.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventHandler {
    private final NotificationMapper notificationMapper;
    private final NotificationSseHub hub;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(NotificationEvent e) {
        try {
            String message = e.type().render(e.title());

            Notification noti = Notification.builder()
                    .userId(e.userId())
                    .type(e.type().name())
                    .targetId(e.targetId())
                    .message(message)
                    .isRead('N')
                    .build();

            notificationMapper.insertNotification(noti);
            Notification saved = notificationMapper.getNotificationById(noti.getNotificationId());
            hub.sendToUser(saved);
        } catch (Exception ex) {
            log.error("[Notification] failed to deliver event={}, cause={}", e, ex.toString(), ex);
        }
    }
}
