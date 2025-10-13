package com.example.funding.exception;

import org.springframework.http.HttpStatus;

public class SettlementAlreadyPaidException extends BusinessException {
    public SettlementAlreadyPaidException() {
        super("이미 정산이 완료된 프로젝트입니다.", HttpStatus.BAD_REQUEST);
    }
}
