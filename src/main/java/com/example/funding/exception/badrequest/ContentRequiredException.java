package com.example.funding.exception.badrequest;

import com.example.funding.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ContentRequiredException extends BusinessException {
    public ContentRequiredException() {
        super("내용은 필수 입력 항목입니다.", HttpStatus.BAD_REQUEST);
    }
}
