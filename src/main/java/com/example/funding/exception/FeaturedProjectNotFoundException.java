package com.example.funding.exception;

import org.springframework.http.HttpStatus;

public class FeaturedProjectNotFoundException extends BusinessException {
    public FeaturedProjectNotFoundException() {
        super("추천 프로젝트가 없습니다.", HttpStatus.NOT_FOUND);
    }
}
