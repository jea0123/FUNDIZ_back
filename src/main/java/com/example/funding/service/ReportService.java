package com.example.funding.service;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.cs.RpAddRequestDto;
import com.example.funding.model.Report;
import org.springframework.http.ResponseEntity;

public interface ReportService {

    ResponseEntity<ResponseDto<PageResult<Report>>> reportList(Pager pager);

    ResponseEntity<ResponseDto<PageResult<Report>>> myReportList(Long userId, Pager pager);

    ResponseEntity<ResponseDto<String>> addReport(Long userId, RpAddRequestDto rpDto);

}