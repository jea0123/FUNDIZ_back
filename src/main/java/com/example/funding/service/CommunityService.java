package com.example.funding.service;


import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.project.CommunityCreateRequestDto;
import com.example.funding.dto.response.project.CommunityDto;
import com.example.funding.common.CursorPage;
import com.example.funding.dto.response.project.ReviewDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Validated
public interface CommunityService {
    ResponseEntity<ResponseDto<CursorPage<CommunityDto>>> getCommunityList(Long projectId, String code, LocalDateTime lastCreatedAt, Long lastId, int size);

    ResponseEntity<ResponseDto<CursorPage<ReviewDto>>> getReviewList(Long projectId, String code, LocalDateTime lastCreatedAt, Long lastId, int size);

    ResponseEntity<ResponseDto<String>> createCommunity(Long projectId, CommunityCreateRequestDto dto, Long userId);
}
