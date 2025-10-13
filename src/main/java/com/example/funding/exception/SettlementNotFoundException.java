package com.example.funding.exception;

import org.springframework.http.HttpStatus;

public class SettlementNotFoundException extends BusinessException {
    public SettlementNotFoundException() {
        super("정산 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
