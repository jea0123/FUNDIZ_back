package com.example.funding.exception;

import org.springframework.http.HttpStatus;

/**
 * 잘못된 자격 증명 예외 클래스
 * <p>
 * 이 예외는 사용자가 로그인 시 잘못된 이메일 또는 비밀번호를 입력했을 때 발생.
 *
 * @author 장민규
 * @apiNote HTTP 상태 코드 400 (BAD REQUEST)을 반환.
 * @since 2025-10-12
 */
public class InvalidCredentialsException extends BusinessException {
    public InvalidCredentialsException() {
        super("이메일 또는 비밀번호가 올바르지 않습니다.", HttpStatus.BAD_REQUEST);
    }
}
