package com.example.funding.exception;

import org.springframework.http.HttpStatus;

public class InCorrectPasswordException extends BusinessException {
    public InCorrectPasswordException() {
        super("현재 비밀번호가 일치하지 않습니다.", HttpStatus.FORBIDDEN);
    }
}
