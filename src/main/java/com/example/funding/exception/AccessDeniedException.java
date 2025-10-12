package com.example.funding.exception;

import org.springframework.http.HttpStatus;

public class AccessDeniedException extends BusinessException {
    public AccessDeniedException() {
        super("접근이 거부되었습니다.", HttpStatus.FORBIDDEN);
    }
}
