package com.example.funding.exception;

import org.springframework.http.HttpStatus;

/**
 * 성공한 프로젝트가 없을 때 발생하는 예외 클래스
 * <p>
 * 이 예외는 특정 카테고리에서 성공한 프로젝트가 없을 경우 발생.
 *
 * @author 장민규
 * @apiNote HTTP 상태 코드 404 (NOT FOUND)를 반환.
 * @since 2025-10-12
 */
public class CategorySuccessNotFoundException extends BusinessException {
    public CategorySuccessNotFoundException() {
        super("성공한 프로젝트가 없습니다.", HttpStatus.NOT_FOUND);
    }
}
