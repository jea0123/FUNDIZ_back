package com.example.funding.exception;

import org.springframework.http.HttpStatus;

public class CreatorNotFoundException extends BusinessException {
    public CreatorNotFoundException() {
        super("크리에이터를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
