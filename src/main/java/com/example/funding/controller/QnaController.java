package com.example.funding.controller;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.creator.QnaReplyCreateRequestDto;
import com.example.funding.dto.request.project.QnaAddRequestDto;
import com.example.funding.common.CursorPage;
import com.example.funding.dto.request.project.ReplyCreateRequestDto;
import com.example.funding.dto.response.cs.InquiryReplyDto;
import com.example.funding.dto.response.cs.QnaReplyDto;
import com.example.funding.dto.response.project.QnaDto;
import com.example.funding.dto.response.project.ReplyDto;
import com.example.funding.service.QnaService;
import com.example.funding.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@Slf4j
@RestController
@RequestMapping("/api/v1/project/{projectId}")
@RequiredArgsConstructor
public class QnaController {

    private final QnaService qnaService;

    /**
     * <p>QnA 내역 목록 조회(프로젝트 상세 페이지 기준)</p>
     *
     * @param projectId 프로젝트 ID
     * @param lastCreatedAt 마지막 항목의 생성일시
     * @param lastId 마지막 항목의 cmId
     * @param size 한 번에 가져올 항목 수
     * @return 성공 시 200 OK
     * @author 이동혁
     * @since 2025-10-07
     */
    @GetMapping("/qna")
    public ResponseEntity<ResponseDto<CursorPage<QnaDto>>> getQnaListOfProject(@PathVariable("projectId") Long projectId,
                                                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastCreatedAt,
                                                                               @RequestParam(required = false) Long lastId,
                                                                               @RequestParam(defaultValue = "10") int size) {

        return qnaService.getQnaListOfProject(projectId, lastCreatedAt, lastId, size);
    }

    /**
     * <p>QnA 질문 등록(프로젝트 상세 페이지 내)</p>
     *
     * @param projectId 프로젝트 ID
     * @param userId 사용자 ID
     * @param qnaDto QnaAddRequestDto
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author 이동혁
     * @since 2025-10-08
     */
    @PostMapping("/qna/{userId}/add")
    public ResponseEntity<ResponseDto<String>> addQuestion(@PathVariable Long projectId, @PathVariable Long userId, @RequestBody QnaAddRequestDto qnaDto){
        return qnaService.addQuestion(projectId, userId, qnaDto);
    }

}
