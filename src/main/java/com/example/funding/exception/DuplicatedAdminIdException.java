package com.example.funding.exception;

import org.springframework.http.HttpStatus;

public class DuplicatedAdminIdException extends BusinessException {
    public DuplicatedAdminIdException() {
        super("이미 존재하는 관리자 아이디입니다.", HttpStatus.CONFLICT);
    }
}
