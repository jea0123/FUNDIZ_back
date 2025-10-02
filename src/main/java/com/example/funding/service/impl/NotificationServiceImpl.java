package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.notification.CreateNotificationRequestDto;
import com.example.funding.mapper.NotificationMapper;
import com.example.funding.mapper.UserMapper;
import com.example.funding.model.Notification;
import com.example.funding.model.User;
import com.example.funding.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationMapper notificationMapper;
    private final UserMapper userMapper;

    /**
     * 사용자 ID로 알림 목록 조회
     *
     * @param userId 사용자 ID
     * @return 알림 목록
     * @throws ResponseStatusException 사용자 없음(404)
     * @author 장민규
     * @since 2025-10-02
     */
    @Override
    public ResponseEntity<ResponseDto<List<Notification>>> getNotificationsByUserId(Long userId) {
        User user = userMapper.getUserById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }
        List<Notification> notifications = notificationMapper.getNotificationsByUserId(userId);
        return ResponseEntity.ok(ResponseDto.success(200, "알림 목록 조회 성공", notifications));
    }

    /**
     * 알림 ID로 단일 알림 조회
     *
     * @param notificationId 알림 ID
     * @return 단일 알림
     * @throws ResponseStatusException 알림 없음(404)
     * @author 장민규
     * @since 2025-10-02
     */
    @Override
    public ResponseEntity<ResponseDto<Notification>> getNotificationById(Long notificationId) {
        Notification notification = notificationMapper.getNotificationById(notificationId);
        if (notification == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "알림을 찾을 수 없습니다.");
        }
        return ResponseEntity.ok(ResponseDto.success(200, "알림 조회 성공", notification));
    }

    /**
     * 알림 생성
     *
     * @param dto 알림 생성 요청 DTO
     * @return 성공 메시지
     * @author 장민규
     * @since 2025-10-02
     */
    @Override
    public ResponseEntity<ResponseDto<Notification>> insertNotification(CreateNotificationRequestDto dto) {
        Notification not = Notification.builder()
                .userId(dto.getUserId())
                .type(dto.getType())
                .targetId(dto.getTargetId())
                .message(dto.getMessage())
                .isRead('N')
                .build();
        Long notificationId = notificationMapper.insertNotification(not);
        Notification notification = notificationMapper.getNotificationById(notificationId);
        return ResponseEntity.ok(ResponseDto.success(201, "알림 생성 성공", notification));
    }

    /**
     * 알림 읽음 처리
     *
     * @param notificationId 알림 ID
     * @param userId 사용자 ID
     * @return 성공 메시지
     * @throws ResponseStatusException 알림 없음(404)
     * @author 장민규
     * @since 2025-10-02
     */
    @Override
    public ResponseEntity<ResponseDto<String>> markAsRead(Long notificationId, Long userId) {
        Notification notification = notificationMapper.getNotificationById(notificationId);
        if (notification == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "알림을 찾을 수 없습니다.");
        }
        if (!notification.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인의 알림만 읽음 처리할 수 있습니다.");
        }
        if (notification.getIsRead() == 'Y') {
            return ResponseEntity.ok(ResponseDto.success(200, "이미 읽음 처리된 알림입니다.", null));
        }
        notificationMapper.markAsRead(notificationId);
        return ResponseEntity.ok(ResponseDto.success(200, "알림 읽음 처리 성공", null));
    }

    /**
     * 사용자 ID로 모든 알림 읽음 처리
     *
     * @param userId 사용자 ID
     * @return 성공 메시지
     * @throws ResponseStatusException 사용자 없음(404)
     * @author 장민규
     * @since 2025-10-02
     */
    @Override
    public ResponseEntity<ResponseDto<String>> markAllAsRead(Long userId) {
        User user = userMapper.getUserById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }
        List<Notification> notifications = notificationMapper.getNotificationsByUserId(userId);
        for (Notification notification : notifications) {
            if (notification.getIsRead() == 'N') {
                notificationMapper.markAsRead(notification.getNotificationId());
            }
        }
        return ResponseEntity.ok(ResponseDto.success(200, "모든 알림 읽음 처리 성공", null));
    }

    /**
     * 알림 삭제
     *
     * @param notificationId 알림 ID
     * @param userId 사용자 ID
     * @return 성공 메시지
     * @throws ResponseStatusException 알림 없음(404)
     * @author 장민규
     * @since 2025-10-02
     */
    @Override
    public ResponseEntity<ResponseDto<String>> deleteNotification(Long notificationId, Long userId) {
        Notification notification = notificationMapper.getNotificationById(notificationId);
        if (notification == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "알림을 찾을 수 없습니다.");
        }
        if (!notification.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인의 알림만 삭제할 수 있습니다.");
        }
        notificationMapper.deleteNotification(notificationId);
        return ResponseEntity.ok(ResponseDto.success(200, "알림 삭제 성공", null));
    }

    /**
     * 사용자 ID로 모든 알림 삭제
     *
     * @param userId 사용자 ID
     * @return 성공 메시지
     * @throws ResponseStatusException 사용자 없음(404)
     * @author 장민규
     * @since 2025-10-02
     */
    @Override
    public ResponseEntity<ResponseDto<String>> deleteAllNotificationsByUserId(Long userId) {
        User user = userMapper.getUserById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }
        notificationMapper.deleteAllNotificationsByUserId(userId);
        return ResponseEntity.ok(ResponseDto.success(200, "모든 알림 삭제 성공", null));
    }
}
