package com.example.funding.exception.badrequest;

import com.example.funding.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class SettlementStatusAlreadyChangedException extends BusinessException {
    public SettlementStatusAlreadyChangedException() {
        super("이미 상태가 변경되었습니다.", HttpStatus.BAD_REQUEST);
    }
}
