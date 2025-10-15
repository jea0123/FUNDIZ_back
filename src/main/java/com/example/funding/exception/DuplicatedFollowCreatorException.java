package com.example.funding.exception;

import org.springframework.http.HttpStatus;

public class DuplicatedFollowCreatorException extends BusinessException {
    public DuplicatedFollowCreatorException() {
        super("이미 팔로우한 크리에이터입니다.", HttpStatus.CONFLICT);
    }
}
