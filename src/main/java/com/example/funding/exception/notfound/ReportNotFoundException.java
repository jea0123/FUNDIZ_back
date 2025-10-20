package com.example.funding.exception.notfound;

import com.example.funding.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ReportNotFoundException extends BusinessException {
    public ReportNotFoundException() { super("신고내역을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
