package com.example.funding.exception;

import org.springframework.http.HttpStatus;

public class ProjectNotFoundException extends BusinessException {
    public ProjectNotFoundException() {
        super("존재하지 않는 프로젝트입니다.", HttpStatus.NOT_FOUND);
    }
}
