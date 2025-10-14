package com.example.funding.controller;

import com.example.funding.common.CursorPage;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.creator.QnaReplyCreateRequestDto;
import com.example.funding.dto.request.project.QnaAddRequestDto;
import com.example.funding.dto.response.cs.QnaReplyDto;
import com.example.funding.dto.response.project.QnaDto;
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
@RequestMapping("/api/v1/qna")
@RequiredArgsConstructor
public class QnaReplyController {

    private final ReplyService replyService;

    @GetMapping("/reply/{qnaId}")
    public ResponseEntity<ResponseDto<CursorPage<QnaReplyDto>>> getInquiryReplyList(
                                                                                        @PathVariable Long qnaId,
                                                                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastCreatedAt,
                                                                                        @RequestParam(required = false) Long lastId,
                                                                                        @RequestParam(defaultValue = "10") int size) {
        return replyService.getQnaReplyList(qnaId, lastCreatedAt, lastId, size);
    }

    @PostMapping("/reply/{qnaId}")
    public ResponseEntity<ResponseDto<QnaReplyDto>> createQnaReply(@PathVariable Long qnaId,
                                                                   @RequestAttribute Long creatorId,
                                                                   @RequestBody QnaReplyCreateRequestDto dto) {

        return replyService.createQnaReply(qnaId, creatorId, dto);
    }
}
