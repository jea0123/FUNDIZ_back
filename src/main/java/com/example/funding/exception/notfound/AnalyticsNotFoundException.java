package com.example.funding.exception.notfound;

import com.example.funding.exception.BusinessException;
import org.springframework.http.HttpStatus;

/**
 * 관리자 대시보드 분석 데이터가 충분하지 않을 때 발생하는 예외 클래스
 * <p>
 * 이 예외는 관리자 대시보드에서 분석 데이터를 조회할 때, 해당 기간 내에 충분한 데이터가 없을 경우 발생.
 *
 * @author 장민규
 * @apiNote HTTP 상태 코드 404 (NOT FOUND)를 반환.
 * @since 2025-10-12
 */
public class AnalyticsNotFoundException extends BusinessException {
    public AnalyticsNotFoundException() {
        super("관리자 대시보드 분석 데이터가 충분하지 않습니다.", HttpStatus.NOT_FOUND);
    }
}
