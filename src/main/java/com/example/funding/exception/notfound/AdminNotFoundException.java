package com.example.funding.exception.notfound;

import com.example.funding.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class AdminNotFoundException extends BusinessException {
    public AdminNotFoundException() {
        super("관리자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
