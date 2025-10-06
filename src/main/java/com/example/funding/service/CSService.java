package com.example.funding.service;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.cs.*;
import com.example.funding.model.Inquiry;
import com.example.funding.model.Notice;
import com.example.funding.model.Report;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CSService {

    ResponseEntity<ResponseDto<PageResult<Notice>>> noticeList(Pager pager);

    ResponseEntity<ResponseDto<Notice>> item(Long noticeId);

    ResponseEntity<ResponseDto<String>> addNotice(NoticeAddRequestDto ntcDto);

    ResponseEntity<ResponseDto<String>> updateNotice(Long noticeId, NoticeUpdateRequestDto ntcDto);

    ResponseEntity<ResponseDto<String>> deleteNotice(Long noticeId);

    ResponseEntity<ResponseDto<PageResult<Inquiry>>> inquiryList(Pager pager);

    ResponseEntity<ResponseDto<PageResult<Inquiry>>> myInquiryList(Long userId, Pager pager);

    ResponseEntity<ResponseDto<String>> addInquiry(Long userId, IqrAddRequestDto iqrDto);

    ResponseEntity<ResponseDto<PageResult<Report>>> reportList(Pager pager);

    ResponseEntity<ResponseDto<PageResult<Report>>> myReportList(Long userId, Pager pager);

    ResponseEntity<ResponseDto<String>> addReport(Long userId, RpAddRequestDto rpDto);

}