package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.project.QnaAddRequestDto;
import com.example.funding.dto.response.project.CursorPage;
import com.example.funding.dto.response.project.QnaDto;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public interface QnaService {

    ResponseEntity<ResponseDto<CursorPage<QnaDto>>> getQnaListOfProject(Long projectId, LocalDateTime lastCreatedAt, Long lastId, int size);

    ResponseEntity<ResponseDto<String>> addQuestion(Long projectId, Long userId, QnaAddRequestDto qnaDto);
}
