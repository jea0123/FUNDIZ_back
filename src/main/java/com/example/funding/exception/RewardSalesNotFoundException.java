package com.example.funding.exception;

import org.springframework.http.HttpStatus;

public class RewardSalesNotFoundException extends BusinessException {
    public RewardSalesNotFoundException() {
        super("리워드 판매 데이터를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
