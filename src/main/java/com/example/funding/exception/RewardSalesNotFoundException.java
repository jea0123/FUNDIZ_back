package com.example.funding.exception;

import org.springframework.http.HttpStatus;

/**
 * 리워드 판매 데이터를 찾을 수 없음 예외 클래스
 * <p>
 * 이 예외는 요청한 리워드 판매 데이터가 존재하지 않을 때 발생.
 *
 * @author 장민규
 * @apiNote HTTP 상태 코드 404 (NOT FOUND)를 반환.
 * @since 2025-10-12
 */
public class RewardSalesNotFoundException extends BusinessException {
    public RewardSalesNotFoundException() {
        super("리워드 판매 데이터를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
