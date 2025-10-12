package com.example.funding.service;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.cs.IqrAddRequestDto;
import com.example.funding.model.Inquiry;
import org.springframework.http.ResponseEntity;

public interface InquiryService {

    ResponseEntity<ResponseDto<PageResult<Inquiry>>> inquiryList(Pager pager);

    ResponseEntity<ResponseDto<PageResult<Inquiry>>> myInquiryList(Long userId, Pager pager);

    ResponseEntity<ResponseDto<String>> addInquiry(Long userId, IqrAddRequestDto iqrDto);

}