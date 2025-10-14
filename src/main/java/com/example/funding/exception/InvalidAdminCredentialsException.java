package com.example.funding.exception;

import org.springframework.http.HttpStatus;

public class InvalidAdminCredentialsException extends BusinessException {
    public InvalidAdminCredentialsException() {
        super("관리자 아이디 또는 비밀번호가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED);
    }
}
