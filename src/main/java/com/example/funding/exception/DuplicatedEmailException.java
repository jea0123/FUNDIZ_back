package com.example.funding.exception;

import org.springframework.http.HttpStatus;

/**
 * 중복된 이메일 예외 클래스
 * <p>
 * 이 예외는 사용자가 이미 존재하는 이메일로 회원가입을 시도할 때 발생.
 *
 * @author 장민규
 * @apiNote HTTP 상태 코드 409 (CONFLICT)를 반환.
 * @since 2025-10-12
 */
public class DuplicatedEmailException extends BusinessException {
    public DuplicatedEmailException() {
        super("이미 사용중인 이메일입니다.", HttpStatus.CONFLICT);
    }
}
