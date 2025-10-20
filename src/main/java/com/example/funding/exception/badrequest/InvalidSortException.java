package com.example.funding.exception.badrequest;

import com.example.funding.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidSortException extends BusinessException {
    public InvalidSortException() {
        super("유효하지 않은 정렬 조건입니다.", HttpStatus.BAD_REQUEST);
    }
}
