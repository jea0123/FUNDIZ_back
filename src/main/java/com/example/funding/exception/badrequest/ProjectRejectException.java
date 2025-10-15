package com.example.funding.exception.badrequest;

import com.example.funding.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ProjectRejectException extends BusinessException {
    public ProjectRejectException() {
        super("이미 처리되었거나 현재 상태에서 반려할 수 없습니다.", HttpStatus.BAD_REQUEST);
    }
}
