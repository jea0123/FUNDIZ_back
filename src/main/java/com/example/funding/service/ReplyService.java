package com.example.funding.service;

import com.example.funding.common.CursorPage;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.project.ReplyCreateRequestDto;
import com.example.funding.dto.response.project.ReplyDto;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public interface ReplyService {
    ResponseEntity<ResponseDto<CursorPage<ReplyDto>>> getReplyList(Long cmId, LocalDateTime lastCreatedAt, Long lastId, int size);

    ResponseEntity<ResponseDto<ReplyDto>> createCommunityReply(Long cmId, ReplyCreateRequestDto dto, Long userId);
}
