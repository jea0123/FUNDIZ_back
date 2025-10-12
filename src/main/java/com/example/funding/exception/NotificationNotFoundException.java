package com.example.funding.exception;

import org.springframework.http.HttpStatus;

public class NotificationNotFoundException extends BusinessException {
    public NotificationNotFoundException() {
        super("알림을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
