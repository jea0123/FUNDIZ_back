package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.model.Notice;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface NoticeService {
    ResponseEntity<ResponseDto<List<Notice>>> list();

    ResponseEntity<ResponseDto<Notice>> getNoticeDetail(Long noticeId);

    ResponseEntity<ResponseDto<Notice>> add(Notice item);

    ResponseEntity<ResponseDto<Notice>> update(Notice item);

    void delete(Long noticeId);


}
