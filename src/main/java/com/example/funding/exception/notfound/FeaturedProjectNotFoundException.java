package com.example.funding.exception.notfound;

import com.example.funding.exception.BusinessException;
import org.springframework.http.HttpStatus;

/**
 * 추천 프로젝트가 없을 때 발생하는 예외 클래스
 * <p>
 * 이 예외는 추천 프로젝트를 조회할 때 해당 프로젝트가 없을 경우 발생.
 *
 * @author 장민규
 * @apiNote HTTP 상태 코드 404 (NOT FOUND)를 반환.
 * @since 2025-10-12
 */
public class FeaturedProjectNotFoundException extends BusinessException {
    public FeaturedProjectNotFoundException() {
        super("추천 프로젝트가 없습니다.", HttpStatus.NOT_FOUND);
    }
}
