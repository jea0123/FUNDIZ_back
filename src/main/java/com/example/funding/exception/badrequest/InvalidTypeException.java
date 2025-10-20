package com.example.funding.exception.badrequest;

import com.example.funding.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidTypeException extends BusinessException {
    public InvalidTypeException() {
        super("허용되지 않는 유형 값입니다.", HttpStatus.BAD_REQUEST);
    }
}
