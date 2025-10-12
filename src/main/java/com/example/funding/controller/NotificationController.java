package com.example.funding.controller;

import com.example.funding.common.CustomUserPrincipal;
import com.example.funding.common.NotificationSseHub;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.notification.CreateNotificationRequestDto;
import com.example.funding.exception.AccessDeniedException;
import com.example.funding.exception.NotificationNotFoundException;
import com.example.funding.exception.UserNotFoundException;
import com.example.funding.model.Notification;
import com.example.funding.service.NotificationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    /**
     * SSE 연결 엔드포인트
     *
     * @param userId   사용자 ID
     * @param response HTTP 응답 객체
     * @return SseEmitter
     * @author by: 장민규
     * @since 2025-10-02
     */
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@RequestParam Long userId, HttpServletResponse response) {
        return hub.register(userId, response);
    }

    /**
     * 모든 알림 읽음 처리
     *
     * @param principal 인증된 사용자 정보
     * @return 성공 메시지
     * @throws UserNotFoundException 사용자 없음
     * @author 장민규
     * @since 2025-10-02
     */
    @GetMapping("/list")
    public ResponseEntity<ResponseDto<List<Notification>>> getAllNotifications(@AuthenticationPrincipal CustomUserPrincipal principal) {
        Long userId = principal.userId();
        userId = 501L;
        return notificationService.getNotificationsByUserId(userId);
    }

    /**
     * 알림 ID로 단일 알림 조회
     *
     * @param notificationId 알림 ID
     * @param principal      인증된 사용자 정보
     * @return 단일 알림
     * @throws UserNotFoundException         사용자 없음
     * @throws AccessDeniedException         접근 권한 없음
     * @throws NotificationNotFoundException 알림 없음
     * @author 장민규
     * @since 2025-10-02
     */
    @GetMapping("/{notificationId}")
    public ResponseEntity<ResponseDto<Notification>> getNotificationById(@PathVariable Long notificationId,
                                                                         @AuthenticationPrincipal CustomUserPrincipal principal) {
        Long userId = principal.userId();
        userId = 501L;
        return notificationService.getNotificationById(notificationId, userId);
    }

    /**
     * 알림 생성
     *
     * @param dto       알림 생성 요청 DTO
     * @param principal 인증된 사용자 정보
     * @return 성공 메시지
     * @author 장민규
     * @since 2025-10-02
     */
    @PostMapping("/create")
    public ResponseEntity<ResponseDto<String>> createNotification(@RequestBody CreateNotificationRequestDto dto,
                                                                  @AuthenticationPrincipal CustomUserPrincipal principal) {
        Long userId = principal.userId();
        userId = 501L;
        dto.setUserId(userId);
        return notificationService.insertNotification(dto);
    }

    /**
     * 알림 읽음 처리
     *
     * @param notificationId 알림 ID
     * @param principal      인증된 사용자 정보
     * @return 성공 메시지
     * @throws UserNotFoundException         사용자 없음
     * @throws AccessDeniedException         접근 권한 없음
     * @throws NotificationNotFoundException 알림 없음
     * @author 장민규
     * @since 2025-10-02
     */
    @PutMapping("/read/{notificationId}")
    public ResponseEntity<ResponseDto<String>> markAsRead(@PathVariable Long notificationId,
                                                          @AuthenticationPrincipal CustomUserPrincipal principal) {
        Long userId = principal.userId();
        userId = 501L;
        return notificationService.markAsRead(notificationId, userId);
    }

    /**
     * 모든 알림 읽음 처리
     *
     * @param principal 인증된 사용자 정보
     * @return 성공 메시지
     * @throws UserNotFoundException 사용자 없음
     * @author 장민규
     * @since 2025-10-02
     */
    @PutMapping("/readAll")
    public ResponseEntity<ResponseDto<String>> markAllAsRead(@AuthenticationPrincipal CustomUserPrincipal principal) {
        Long userId = principal.userId();
        userId = 501L;
        return notificationService.markAllAsRead(userId);
    }

    /**
     * 알림 삭제
     *
     * @param notificationId 알림 ID
     * @param principal      인증된 사용자 정보
     * @return 성공 메시지
     * @throws UserNotFoundException         사용자 없음
     * @throws AccessDeniedException         접근 권한 없음
     * @throws NotificationNotFoundException 알림 없음
     * @author 장민규
     * @since 2025-10-02
     */
    @DeleteMapping("/delete/{notificationId}")
    public ResponseEntity<ResponseDto<String>> deleteNotification(@PathVariable Long notificationId,
                                                                  @AuthenticationPrincipal CustomUserPrincipal principal) {
        Long userId = principal.userId();
        userId = 501L;
        return notificationService.deleteNotification(notificationId, userId);
    }

    /**
     * 모든 알림 삭제
     *
     * @param principal 인증된 사용자 정보
     * @return 성공 메시지
     * @throws UserNotFoundException 사용자 없음
     * @throws AccessDeniedException 접근 권한 없음
     * @author 장민규
     * @since 2025-10-02
     */
    @DeleteMapping("/deleteAll")
    public ResponseEntity<ResponseDto<String>> deleteAllNotifications(@AuthenticationPrincipal CustomUserPrincipal principal) {
        Long userId = principal.userId();
        userId = 501L;
        return notificationService.deleteAllNotificationsByUserId(userId);
    }

    @ExceptionHandler({AsyncRequestNotUsableException.class, ClientAbortException.class, IOException.class})
    public ResponseEntity<Void> handleClientDisconnect(Exception ignored) {
//        log.debug("클라이언트 연결이 끊어짐: {}", ex.getMessage());
        return ResponseEntity.noContent().build();
    }
}
