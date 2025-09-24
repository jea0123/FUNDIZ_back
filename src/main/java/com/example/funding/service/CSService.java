package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.model.Inquiry;
import com.example.funding.model.Notice;
import com.example.funding.model.Report;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CSService {
    ResponseEntity<ResponseDto<List<Notice>>> noticeList();

    ResponseEntity<ResponseDto<Notice>> item(Long noticeId);

    ResponseEntity<ResponseDto<String>> addNotice(Notice notice);

    ResponseEntity<ResponseDto<String>> updateNotice(Long noticeId, Notice notice);

    ResponseEntity<ResponseDto<String>> deleteNotice(Long noticeId);

    ResponseEntity<ResponseDto<List<Inquiry>>> inquiryList();

    ResponseEntity<ResponseDto<List<Report>>> reportList();
}