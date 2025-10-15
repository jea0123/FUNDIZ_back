package com.example.funding.exception.badrequest;

import com.example.funding.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class AlreadyCreatorException extends BusinessException {
    public AlreadyCreatorException() {
        super("이미 크리에이터로 등록된 유저입니다.", HttpStatus.BAD_REQUEST);
    }
}
