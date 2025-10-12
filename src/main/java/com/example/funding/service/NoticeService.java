package com.example.funding.service;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.cs.*;
import com.example.funding.model.Notice;
import org.springframework.http.ResponseEntity;

public interface NoticeService {

    ResponseEntity<ResponseDto<PageResult<Notice>>> noticeList(Pager pager);

    ResponseEntity<ResponseDto<Notice>> item(Long noticeId);

    ResponseEntity<ResponseDto<String>> addNotice(NoticeAddRequestDto ntcDto);

    ResponseEntity<ResponseDto<String>> updateNotice(Long noticeId, NoticeUpdateRequestDto ntcDto);

    ResponseEntity<ResponseDto<String>> deleteNotice(Long noticeId);

}