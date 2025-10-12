package com.example.funding.exception;

import org.springframework.http.HttpStatus;

public class AnalyticsNotFoundException extends BusinessException {
    public AnalyticsNotFoundException() {
        super("관리자 대시보드 분석 데이터가 충분하지 않습니다.", HttpStatus.NOT_FOUND);
    }
}
