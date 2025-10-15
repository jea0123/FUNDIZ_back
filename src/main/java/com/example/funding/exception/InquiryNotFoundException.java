package com.example.funding.exception;

import org.springframework.http.HttpStatus;

public class InquiryNotFoundException extends BusinessException {
    public InquiryNotFoundException() {
        super("문의글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
