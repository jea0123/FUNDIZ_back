package com.example.funding.service;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.project.QnaAddRequestDto;
import com.example.funding.dto.response.project.CommunityDto;
import com.example.funding.dto.response.project.CursorPage;
import com.example.funding.dto.response.project.ReviewDto;
import com.example.funding.model.Qna;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public interface CommunityService {
    ResponseEntity<ResponseDto<CursorPage<CommunityDto>>> getCommunityList(Long projectId, String code, LocalDateTime lastCreatedAt, Long lastId, int size);

    ResponseEntity<ResponseDto<CursorPage<ReviewDto>>> getReviewList(Long projectId, String code, LocalDateTime lastCreatedAt, Long lastId, int size);

    ResponseEntity<ResponseDto<PageResult<Qna>>> getQnaListOfPJ(Long projectId, Pager pager);

    ResponseEntity<ResponseDto<String>> addQuestion(Long projectId, Long userId, QnaAddRequestDto qnaDto);
}
