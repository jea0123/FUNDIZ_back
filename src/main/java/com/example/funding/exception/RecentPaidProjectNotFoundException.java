package com.example.funding.exception;

import org.springframework.http.HttpStatus;

public class RecentPaidProjectNotFoundException extends BusinessException {
    public RecentPaidProjectNotFoundException() {
        super("최근 24시간 내 결제된 프로젝트가 없습니다.", HttpStatus.NOT_FOUND);
    }
}
