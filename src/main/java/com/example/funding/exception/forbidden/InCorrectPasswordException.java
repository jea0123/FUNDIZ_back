package com.example.funding.exception.forbidden;

import com.example.funding.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InCorrectPasswordException extends BusinessException {
    public InCorrectPasswordException() {
        super("현재 비밀번호가 일치하지 않습니다.", HttpStatus.FORBIDDEN);
    }
}
