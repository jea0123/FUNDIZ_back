package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.notification.CreateNotificationRequestDto;
import com.example.funding.model.Notification;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface NotificationService {
    ResponseEntity<ResponseDto<List<Notification>>> getNotificationsByUserId(Long userId);

    ResponseEntity<ResponseDto<Notification>> getNotificationById(Long notificationId, Long userId);

    ResponseEntity<ResponseDto<String>> insertNotification(CreateNotificationRequestDto dto);

    ResponseEntity<ResponseDto<String>> markAsRead(Long notificationId, Long userId);

    ResponseEntity<ResponseDto<String>> markAllAsRead(Long userId);

    ResponseEntity<ResponseDto<String>> deleteNotification(Long notificationId, Long userId);

    ResponseEntity<ResponseDto<String>> deleteAllNotificationsByUserId(Long userId);
}
