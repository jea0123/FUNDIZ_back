package com.example.funding.exception;

import org.springframework.http.HttpStatus;

public class FollowingCreatorNotFoundException extends BusinessException {
    public FollowingCreatorNotFoundException() {
        super("팔로잉 중인 크리에이터가 아닙니다.", HttpStatus.NOT_FOUND);
    }
}
