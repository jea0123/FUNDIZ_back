package com.example.funding.exception;

import org.springframework.http.HttpStatus;

public class SettlementStatusAlreadyChangedException extends BusinessException {
    public SettlementStatusAlreadyChangedException() {
        super("이미 상태가 변경되었습니다.", HttpStatus.BAD_REQUEST);
    }
}
