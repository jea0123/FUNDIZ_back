package com.example.funding.exception;

import org.springframework.http.HttpStatus;

public class CreatorNotFountException extends BusinessException {
    public CreatorNotFountException() {
        super("크리에이터를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
