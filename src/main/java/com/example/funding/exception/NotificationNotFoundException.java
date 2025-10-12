package com.example.funding.exception;

import org.springframework.http.HttpStatus;

/**
 * 알림을 찾을 수 없음 예외 클래스
 * <p>
 * 이 예외는 요청한 알림이 존재하지 않을 때 발생.
 *
 * @author 장민규
 * @apiNote HTTP 상태 코드 404 (NOT FOUND)를 반환.
 * @since 2025-10-12
 */
public class NotificationNotFoundException extends BusinessException {
    public NotificationNotFoundException() {
        super("알림을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
