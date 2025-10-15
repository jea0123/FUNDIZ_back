package com.example.funding.service.impl;

import com.example.funding.common.NotificationSseHub;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.notification.CreateNotificationRequestDto;
import com.example.funding.exception.AccessDeniedException;
import com.example.funding.exception.NotificationNotFoundException;
import com.example.funding.exception.UserNotFoundException;
import com.example.funding.mapper.NotificationMapper;
import com.example.funding.mapper.UserMapper;
import com.example.funding.model.Notification;
import com.example.funding.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationSseHub hub;
    private final NotificationMapper notificationMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<List<Notification>>> getNotificationsByUserId(Long userId) {
        if (userMapper.getUserById(userId) == null) throw new UserNotFoundException();

        List<Notification> notifications = notificationMapper.getNotificationsByUserId(userId);
        return ResponseEntity.ok(ResponseDto.success(200, "알림 목록 조회 성공", notifications));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<Notification>> getNotificationById(Long notificationId, Long userId) {
        Notification notification = notificationMapper.getNotificationById(notificationId);
        if (notification == null) throw new NotificationNotFoundException();

        if (!notification.getUserId().equals(userId)) throw new AccessDeniedException();

        return ResponseEntity.ok(ResponseDto.success(200, "알림 조회 성공", notification));
    }

    @Override
    public ResponseEntity<ResponseDto<String>> insertNotification(CreateNotificationRequestDto dto) {
        Notification noti = Notification.builder()
                .userId(dto.getUserId())
                .type(dto.getType())
                .targetId(dto.getTargetId())
                .message(dto.getMessage())
                .isRead('N')
                .build();
        notificationMapper.insertNotification(noti);
        Long notificationId = noti.getNotificationId();
        Notification notification = notificationMapper.getNotificationById(notificationId);
        hub.sendToUser(notification);

        return ResponseEntity.ok(ResponseDto.success(201, "알림 생성 성공", dto.getMessage()));
    }

    @Override
    public ResponseEntity<ResponseDto<String>> markAsRead(Long notificationId, Long userId) {
        Notification notification = notificationMapper.getNotificationById(notificationId);
        if (notification == null) throw new NotificationNotFoundException();

        if (!notification.getUserId().equals(userId)) throw new AccessDeniedException();

        if (notification.getIsRead() == 'Y') {
            return ResponseEntity.ok(ResponseDto.success(200, "이미 읽음 처리된 알림입니다.", null));
        }
        notificationMapper.markAsRead(notificationId);
        return ResponseEntity.ok(ResponseDto.success(200, "알림 읽음 처리 성공", null));
    }

    @Override
    public ResponseEntity<ResponseDto<String>> markAllAsRead(Long userId) {
        if (userMapper.getUserById(userId) == null) throw new UserNotFoundException();

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

    @Override
    public ResponseEntity<ResponseDto<String>> deleteNotification(Long notificationId, Long userId) {
        Notification notification = notificationMapper.getNotificationById(notificationId);
        if (notification == null) throw new NotificationNotFoundException();

        if (!notification.getUserId().equals(userId)) throw new AccessDeniedException();

        notificationMapper.deleteNotification(notificationId);
        return ResponseEntity.ok(ResponseDto.success(200, "알림 삭제 성공", null));
    }

    @Override
    public ResponseEntity<ResponseDto<String>> deleteAllNotificationsByUserId(Long userId) {
        if (userMapper.getUserById(userId) == null) throw new UserNotFoundException();

        notificationMapper.deleteAllNotificationsByUserId(userId);
        return ResponseEntity.ok(ResponseDto.success(200, "모든 알림 삭제 성공", null));
    }
}
