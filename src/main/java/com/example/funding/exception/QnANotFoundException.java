package com.example.funding.exception;

import org.springframework.http.HttpStatus;

public class QnANotFoundException extends BusinessException {
    public QnANotFoundException() {
        super("QnA를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
