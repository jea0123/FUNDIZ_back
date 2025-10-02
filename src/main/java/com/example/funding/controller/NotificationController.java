package com.example.funding.controller;

import com.example.funding.common.CustomUserPrincipal;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.notification.CreateNotificationRequestDto;
import com.example.funding.model.Notification;
import com.example.funding.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final ConcurrentHashMap<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final NotificationService notificationService;

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@RequestParam Long userId) {
        SseEmitter emitter = new SseEmitter(60 * 1000L); // 1분 타임아웃
        emitters.put(userId, emitter);

        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));
        emitter.onError((e) -> emitters.remove(userId));

        try {
            emitter.send(SseEmitter.event()
                    .name("INIT")
                    .data("connected"));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try {
                emitter.send(":\n\n");
            } catch (Exception ignore) {}
        }, 15, 15, TimeUnit.SECONDS);

        return emitter;
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseDto<List<Notification>>> getAllNotifications(@AuthenticationPrincipal CustomUserPrincipal principal) {
        return notificationService.getNotificationsByUserId(principal.userId());
    }

    @GetMapping("/{notificationId}")
    public ResponseEntity<ResponseDto<Notification>> getNotificationById(@PathVariable Long notificationId) {
        return notificationService.getNotificationById(notificationId);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDto<Notification>> createNotification(@RequestBody CreateNotificationRequestDto dto, @AuthenticationPrincipal CustomUserPrincipal principal) {
        dto.setUserId(principal.userId());
        return notificationService.insertNotification(dto);
    }

    @PutMapping("/read/{notificationId}")
    public ResponseEntity<ResponseDto<String>> markAsRead(@PathVariable Long notificationId, @AuthenticationPrincipal CustomUserPrincipal principal) {
        return notificationService.markAsRead(notificationId, principal.userId());
    }

    @PutMapping("/readAll")
    public ResponseEntity<ResponseDto<String>> markAllAsRead(@AuthenticationPrincipal CustomUserPrincipal principal) {
        return notificationService.markAllAsRead(principal.userId());
    }

    @DeleteMapping("/delete/{notificationId}")
    public ResponseEntity<ResponseDto<String>> deleteNotification(@PathVariable Long notificationId, @AuthenticationPrincipal CustomUserPrincipal principal) {
        return notificationService.deleteNotification(notificationId, principal.userId());
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<ResponseDto<String>> deleteAllNotifications(@AuthenticationPrincipal CustomUserPrincipal principal) {
        return notificationService.deleteAllNotificationsByUserId(principal.userId());
    }

    public void sendToUser(Notification noti) {
        SseEmitter emitter = emitters.get(noti.getUserId());
        if (emitter == null) return;

        try {
            String json = objectMapper.writeValueAsString(noti);
            emitter.send(SseEmitter.event()
                    .name("NOTIFICATION")
                    .data(json));

            // emitter.send(SseEmitter.event().name("NOTIFICATION").data(noti, MediaType.APPLICATION_JSON));

        } catch (IOException e) {
            emitter.completeWithError(e);
            emitters.remove(noti.getUserId());
        }
    }
}
