package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.exception.forbidden.AccessDeniedException;
import com.example.funding.mapper.NotificationMapper;
import com.example.funding.model.Notification;
import com.example.funding.service.NotificationService;
import com.example.funding.validator.Loaders;
import com.example.funding.validator.PermissionChecker;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@Validated
public class NotificationServiceImpl implements NotificationService {
    private final Loaders loaders;
    private final PermissionChecker auth;
    private final NotificationMapper notificationMapper;

    /**
     * 알림 목록 조회
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<List<Notification>>> getNotificationsByUserId(@NotBlank Long userId) {
        loaders.user(userId);

        List<Notification> notifications = notificationMapper.getNotificationsByUserId(userId);
        return ResponseEntity.ok(ResponseDto.success(200, "알림 목록 조회 성공", notifications));
    }

    /**
     * 알림 단건 조회
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<Notification>> getNotificationById(@NotBlank Long notificationId, @NotBlank Long userId) {
        loaders.user(userId);
        Notification notification = loaders.notification(notificationId);
        auth.mustBeOwner(userId, notification.getUserId());

        return ResponseEntity.ok(ResponseDto.success(200, "알림 조회 성공", notification));
    }

    /**
     * 알림 읽음 처리
     */
    @Override
    public ResponseEntity<ResponseDto<String>> markAsRead(@NotBlank Long notificationId, @NotBlank Long userId) {
        loaders.user(userId);
        Notification notification = loaders.notification(notificationId);
        auth.mustBeOwner(userId, notification.getUserId());

        if (notification.getIsRead() == 'Y') {
            return ResponseEntity.ok(ResponseDto.success(200, "이미 읽음 처리된 알림입니다.", null));
        }
        notificationMapper.markAsRead(notificationId);
        return ResponseEntity.ok(ResponseDto.success(200, "알림 읽음 처리 성공", null));
    }

    /**
     * 모든 알림 읽음 처리
     */
    @Override
    public ResponseEntity<ResponseDto<String>> markAllAsRead(@NotBlank Long userId) {
        loaders.user(userId);

        List<Notification> notifications = notificationMapper.getNotificationsByUserId(userId);
        for (Notification notification : notifications) {
            if (notification.getIsRead() == 'N') {
                if (!notification.getUserId().equals(userId)) {
                    throw new AccessDeniedException();
                }
                notificationMapper.markAsRead(notification.getNotificationId());
            }
        }
        return ResponseEntity.ok(ResponseDto.success(200, "모든 알림 읽음 처리 성공", null));
    }

    /**
     * 알림 삭제
     */
    @Override
    public ResponseEntity<ResponseDto<String>> deleteNotification(@NotBlank Long notificationId, @NotBlank Long userId) {
        loaders.user(userId);
        Notification notification = loaders.notification(notificationId);
        auth.mustBeOwner(userId, notification.getUserId());

        notificationMapper.deleteNotification(notificationId);
        return ResponseEntity.ok(ResponseDto.success(200, "알림 삭제 성공", null));
    }

    /**
     * 모든 알림 삭제
     */
    @Override
    public ResponseEntity<ResponseDto<String>> deleteAllNotificationsByUserId(@NotBlank Long userId) {
        loaders.user(userId);

        notificationMapper.deleteAllNotificationsByUserId(userId);
        return ResponseEntity.ok(ResponseDto.success(200, "모든 알림 삭제 성공", null));
    }
}
