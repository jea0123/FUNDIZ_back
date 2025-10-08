package com.example.funding.controller;

import com.example.funding.common.NotificationSseHub;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.notification.CreateNotificationRequestDto;
import com.example.funding.model.Notification;
import com.example.funding.service.NotificationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationSseHub hub;
    private final NotificationService notificationService;

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
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

    @ExceptionHandler({AsyncRequestNotUsableException.class, ClientAbortException.class, IOException.class})
    public ResponseEntity<Void> handleClientDisconnect(Exception ex) {
        log.debug("클라이언트 연결이 끊어짐: {}", ex.getMessage());
        return ResponseEntity.noContent().build();
    }
}
