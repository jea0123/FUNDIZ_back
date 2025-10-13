package com.example.funding.exception;

import org.springframework.http.HttpStatus;

public class ProjectNotSuccessException extends BusinessException {
    public ProjectNotSuccessException() {
        super("프로젝트가 성공하지 못했습니다.", HttpStatus.BAD_REQUEST);
    }
}
