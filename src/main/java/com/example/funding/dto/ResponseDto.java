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

    public static <T> ResponseDto<T> success(int code, String message, T data) {
        return new ResponseDto<>(code, message, data);
    }

    public static <T> ResponseDto<T> fail(int code, String message) {
        return new ResponseDto<>(code, message, null);
    }
}
