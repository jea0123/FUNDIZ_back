package com.example.funding.exception.badrequest;

import com.example.funding.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidParamException extends BusinessException {
    public InvalidParamException() {
        super("유효하지 않은 파라미터입니다.", HttpStatus.BAD_REQUEST);
    }
}
