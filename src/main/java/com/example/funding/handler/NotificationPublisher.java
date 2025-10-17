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

    public void publish(Long userId, NotificationType type, String title, Long targetId) {
        String key = NotificationEvent.defaultIdemKey(userId, type, targetId);
        publisher.publishEvent(new NotificationEvent(userId, type, title, targetId, key));
    }
}
