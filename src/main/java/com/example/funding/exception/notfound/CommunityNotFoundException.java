package com.example.funding.exception.notfound;

import com.example.funding.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class CommunityNotFoundException extends BusinessException {
    public CommunityNotFoundException() {
        super("커뮤니티를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
