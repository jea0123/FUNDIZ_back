package com.example.funding.exception.badrequest;

import com.example.funding.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidStatusException extends BusinessException {
    public InvalidStatusException() {
        super("허용되지 않는 상태 값입니다.", HttpStatus.BAD_REQUEST);
    }
}
