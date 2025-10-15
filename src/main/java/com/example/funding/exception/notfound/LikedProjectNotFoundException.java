package com.example.funding.exception.notfound;

import com.example.funding.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class LikedProjectNotFoundException extends BusinessException {
    public LikedProjectNotFoundException() {
        super("해당 사용자가 좋아요한 프로젝트가 아닙니다.", HttpStatus.NOT_FOUND);
    }
}
