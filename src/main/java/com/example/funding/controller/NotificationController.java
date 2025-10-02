package com.example.funding.controller;

import com.example.funding.common.NotificationSseHub;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.notification.CreateNotificationRequestDto;
import com.example.funding.model.Notification;
import com.example.funding.service.NotificationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationSseHub hub;
    private static final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
    private static final Map<Long, ScheduledFuture<?>> heartbeats = new ConcurrentHashMap<>();
    private final NotificationService notificationService;

    @GetMapping(value="/stream", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@RequestParam Long userId, HttpServletResponse response) {
        return hub.register(userId, response);
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseDto<List<Notification>>> getAllNotifications() {
        return notificationService.getNotificationsByUserId(501L);
    }

    @GetMapping("/{notificationId}")
    public ResponseEntity<ResponseDto<Notification>> getNotificationById(@PathVariable Long notificationId) {
        return notificationService.getNotificationById(notificationId, 501L);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDto<String>> createNotification(@RequestBody CreateNotificationRequestDto dto) {
        dto.setUserId(501L);
        return notificationService.insertNotification(dto);
    }

    @PutMapping("/read/{notificationId}")
    public ResponseEntity<ResponseDto<String>> markAsRead(@PathVariable Long notificationId) {
        return notificationService.markAsRead(notificationId, 501L);
    }

    @PutMapping("/readAll")
    public ResponseEntity<ResponseDto<String>> markAllAsRead() {
        return notificationService.markAllAsRead(501L);
    }

    @DeleteMapping("/delete/{notificationId}")
    public ResponseEntity<ResponseDto<String>> deleteNotification(@PathVariable Long notificationId) {
        return notificationService.deleteNotification(notificationId, 501L);
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<ResponseDto<String>> deleteAllNotifications() {
        return notificationService.deleteAllNotificationsByUserId(501L);
    }
}
