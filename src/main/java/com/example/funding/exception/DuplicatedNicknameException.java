package com.example.funding.exception;

import org.springframework.http.HttpStatus;

public class DuplicatedNicknameException extends BusinessException {
    public DuplicatedNicknameException() {
        super("이미 사용중인 닉네임입니다.", HttpStatus.CONFLICT);
    }
}
