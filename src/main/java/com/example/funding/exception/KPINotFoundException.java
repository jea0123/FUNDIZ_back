package com.example.funding.exception;

import org.springframework.http.HttpStatus;

public class KPINotFoundException extends BusinessException {
    public KPINotFoundException() {
        super("KPI 데이터를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
