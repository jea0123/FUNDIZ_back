package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.exception.forbidden.AccessDeniedException;
import com.example.funding.exception.notfound.NotificationNotFoundException;
import com.example.funding.exception.notfound.UserNotFoundException;
import com.example.funding.model.Notification;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface NotificationService {
    /**
     * 사용자 ID로 알림 목록 조회
     *
     * @param userId 사용자 ID
     * @return 알림 목록
     * @throws UserNotFoundException 사용자 없음
     * @author 장민규
     * @since 2025-10-02
     */
    ResponseEntity<ResponseDto<List<Notification>>> getNotificationsByUserId(Long userId);

    /**
     * 알림 ID로 단일 알림 조회
     *
     * @param notificationId 알림 ID
     * @return 단일 알림
     * @throws NotificationNotFoundException 알림 없음
     * @throws AccessDeniedException         접근 권한 없음
     * @author 장민규
     * @since 2025-10-02
     */
    ResponseEntity<ResponseDto<Notification>> getNotificationById(Long notificationId, Long userId);

    /**
     * 알림 읽음 처리
     *
     * @param notificationId 알림 ID
     * @param userId         사용자 ID
     * @return 성공 메시지
     * @throws NotificationNotFoundException 알림 없음
     * @throws AccessDeniedException         접근 권한 없음
     * @author 장민규
     * @since 2025-10-02
     */
    ResponseEntity<ResponseDto<String>> markAsRead(Long notificationId, Long userId);

    /**
     * 사용자 ID로 모든 알림 읽음 처리
     *
     * @param userId 사용자 ID
     * @return 성공 메시지
     * @throws UserNotFoundException 사용자 없음
     * @throws AccessDeniedException 접근 권한 없음
     * @author 장민규
     * @since 2025-10-02
     */
    ResponseEntity<ResponseDto<String>> markAllAsRead(Long userId);

    /**
     * 알림 삭제
     *
     * @param notificationId 알림 ID
     * @param userId         사용자 ID
     * @return 성공 메시지
     * @throws NotificationNotFoundException 알림 없음
     * @throws AccessDeniedException         접근 권한 없음
     * @author 장민규
     * @since 2025-10-02
     */
    ResponseEntity<ResponseDto<String>> deleteNotification(Long notificationId, Long userId);

    /**
     * 사용자 ID로 모든 알림 삭제
     *
     * @param userId 사용자 ID
     * @return 성공 메시지
     * @throws UserNotFoundException 사용자 없음
     * @author 장민규
     * @since 2025-10-02
     */
    ResponseEntity<ResponseDto<String>> deleteAllNotificationsByUserId(Long userId);
}
