package com.example.funding.exception.notfound;

import com.example.funding.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class BackingNotFoundException extends BusinessException {
    public BackingNotFoundException() {
        super("해당 후원 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
