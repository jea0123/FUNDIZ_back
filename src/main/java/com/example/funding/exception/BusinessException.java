package com.example.funding.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 비즈니스 로직 예외의 추상 클래스
 * <p>
 * 이 클래스는 모든 비즈니스 로직 관련 예외의 기본 클래스.
 * <p>
 * 구체적인 예외 클래스들은 이 클래스를 상속받아 구현.
 *
 * @author 장민규
 * @apiNote 각 예외는 적절한 HTTP 상태 코드를 포함.
 * @since 2025-10-12
 */
@Getter
public abstract class BusinessException extends RuntimeException {
    private final HttpStatus status;

    protected BusinessException(String message, HttpStatus httpStatus) {
        super(message);
        this.status = httpStatus;
    }
}
