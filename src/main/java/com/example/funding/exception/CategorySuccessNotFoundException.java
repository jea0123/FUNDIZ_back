package com.example.funding.exception;

import org.springframework.http.HttpStatus;

public class CategorySuccessNotFoundException extends BusinessException {
    public CategorySuccessNotFoundException() {
        super("성공한 프로젝트가 없습니다.", HttpStatus.NOT_FOUND);
    }
}
