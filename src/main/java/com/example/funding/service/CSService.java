package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.model.Notice;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CSService {
    ResponseEntity<ResponseDto<List<Notice>>> noticeList();

    ResponseEntity<ResponseDto<Notice>> item(Long noticeId);

    void add(ResponseEntity<ResponseDto<Notice>> item);
}