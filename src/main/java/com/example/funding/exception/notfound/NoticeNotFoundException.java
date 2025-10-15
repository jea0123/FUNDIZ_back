package com.example.funding.exception.notfound;

import com.example.funding.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NoticeNotFoundException extends BusinessException {
    public NoticeNotFoundException() {
        super("공지사항을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
