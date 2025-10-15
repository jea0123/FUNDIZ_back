package com.example.funding.exception.conflict;

import com.example.funding.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DuplicatedLikedProjectException extends BusinessException {
    public DuplicatedLikedProjectException() {
        super("이미 좋아요한 프로젝트입니다.", HttpStatus.CONFLICT);
    }
}
