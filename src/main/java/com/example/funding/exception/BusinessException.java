package com.example.funding.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BusinessException extends RuntimeException {
    private final HttpStatus status;

    protected BusinessException(String message, HttpStatus httpStatus) {
        super(message);
        this.status = httpStatus;
    }
}
