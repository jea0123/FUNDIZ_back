package com.example.funding.exception.notfound;

import com.example.funding.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class RewardNotFoundException extends BusinessException {
    public RewardNotFoundException() {
        super("리워드를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
