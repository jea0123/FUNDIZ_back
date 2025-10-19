package com.example.funding.handler;

import com.example.funding.common.NotificationEvent;
import com.example.funding.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationPublisher {
    private final ApplicationEventPublisher publisher;

    /**
     * 알림 이벤트 발행
     *
     * @param userId   알림을 받을 사용자 ID
     * @param type     알림 유형
     * @param title    알림 제목
     * @param targetId 알림 대상 ID
     */
    public void publish(Long userId, NotificationType type, String title, Long targetId) {
        String key = NotificationEvent.defaultIdemKey(userId, type, targetId);
        publisher.publishEvent(new NotificationEvent(userId, type, title, targetId, key));
    }
}
