package com.example.funding.exception;

import org.springframework.http.HttpStatus;

public class DuplicatedEmailException extends BusinessException {
    public DuplicatedEmailException() {
        super("이미 사용중인 이메일입니다.", HttpStatus.CONFLICT);
    }
}
