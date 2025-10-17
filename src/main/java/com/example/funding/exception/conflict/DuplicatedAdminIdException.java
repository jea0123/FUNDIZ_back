package com.example.funding.exception.conflict;

import com.example.funding.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DuplicatedAdminIdException extends BusinessException {
    public DuplicatedAdminIdException() {
        super("이미 존재하는 관리자 아이디입니다.", HttpStatus.CONFLICT);
    }
}
