package com.example.funding.exception;

import org.springframework.http.HttpStatus;

public class AddressNotFoundException extends BusinessException {
    public AddressNotFoundException() {
        super("배송지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
