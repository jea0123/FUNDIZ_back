package com.example.funding.exception.forbidden;

import com.example.funding.exception.BusinessException;
import org.springframework.http.HttpStatus;

/**
 * 접근 거부 예외 클래스
 * <p>
 * 이 예외는 사용자가 권한이 없는 리소스에 접근하려고 할 때 발생.
 *
 * @author 장민규
 * @apiNote HTTP 상태 코드 403 (FORBIDDEN)을 반환.
 * @since 2025-10-12
 */
public class AccessDeniedException extends BusinessException {
    public AccessDeniedException() {
        super("접근이 거부되었습니다.", HttpStatus.FORBIDDEN);
    }
}
