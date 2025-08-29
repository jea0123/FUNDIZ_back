package com.example.funding.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ResponseDto<T> {
    private int code;
    private String message;
    private T data;

    /**
     * <p>성공 응답 생성 메서드</p>
     * @param code    응답 코드
     * @param message 응답 메시지
     * @param data    응답 데이터
     * @param <T>     데이터 타입
     * @return 성공 응답 DTO
     * @since 2025-08-27
     * @author 장민규
     */
    public static <T> ResponseDto<T> success(int code, String message, T data) {
        return new ResponseDto<>(code, message, data);
    }

    /**
     * <p>실패 응답 생성 메서드</p>
     * @param code    응답 코드
     * @param message 응답 메시지
     * @param <T>     데이터 타입
     * @return 실패 응답 DTO
     * @since 2025-08-27
     * @author 장민규
     */
    public static <T> ResponseDto<T> fail(int code, String message) {
        return new ResponseDto<>(code, message, null);
    }
}
