package com.example.funding.service;

import com.example.funding.common.CursorPage;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.creator.QnaReplyCreateRequestDto;
import com.example.funding.dto.request.cs.IqrReplyCreateRequestDto;
import com.example.funding.dto.request.project.ReplyCreateRequestDto;
import com.example.funding.dto.response.cs.InquiryReplyDto;
import com.example.funding.dto.response.cs.QnaReplyDto;
import com.example.funding.dto.response.project.ReplyDto;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public interface ReplyService {
    ResponseEntity<ResponseDto<CursorPage<ReplyDto>>> getReplyList(Long cmId, LocalDateTime lastCreatedAt, Long lastId, int size);

    ResponseEntity<ResponseDto<ReplyDto>> createCommunityReply(Long cmId, ReplyCreateRequestDto dto, Long userId);

    ResponseEntity<ResponseDto<CursorPage<InquiryReplyDto>>> getInquiryReplyList(Long inqId, LocalDateTime lastCreatedAt, Long lastId, int size);

    ResponseEntity<ResponseDto<InquiryReplyDto>> createInquiryReply(Long inqId, IqrReplyCreateRequestDto dto);

    ResponseEntity<ResponseDto<QnaReplyDto>> createQnaReply(Long qnaId, Long creatorId, QnaReplyCreateRequestDto dto);

    ResponseEntity<ResponseDto<CursorPage<QnaReplyDto>>> getQnaReplyList(Long qnaId, LocalDateTime lastCreatedAt, Long lastId, int size);
}
