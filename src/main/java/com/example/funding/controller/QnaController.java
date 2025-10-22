package com.example.funding.controller;

import com.example.funding.common.CursorPage;
import com.example.funding.common.CustomUserPrincipal;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.creator.QnaReplyCreateRequestDto;
import com.example.funding.dto.response.cs.QnaReplyDto;
import com.example.funding.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@Slf4j
@RestController
@RequestMapping("/api/v1/qna")
@RequiredArgsConstructor
public class QnaController {

    private final ReplyService replyService;

    /**
     * <p>Q&A 답변 조회(필요없음)</p>
     *
     * @param qnaId         Q&A ID
     * @param lastCreatedAt 마지막 항목의 생성일시
     * @param lastId        마지막 항목의 id
     * @param size          한 번에 가져올 항목 수
     * @return 성공 시 200 OK
     * @author 이동혁
     * @since 2025-10-14
     */
    @GetMapping("/reply/{qnaId}")
    public ResponseEntity<ResponseDto<CursorPage<QnaReplyDto>>> getInquiryReplyList(@PathVariable Long qnaId,
                                                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastCreatedAt,
                                                                                    @RequestParam(required = false) Long lastId,
                                                                                    @RequestParam(defaultValue = "10") int size) {
        return replyService.getQnaReplyList(qnaId, lastCreatedAt, lastId, size);
    }

    /**
     * <p>Q&A 답변 등록(관리자)</p>
     *
     * @param qnaId 커뮤니티 ID
     * @param dto   QnaReplyCreateRequestDto
     * @return 성공 시 200 OK
     * @author 이동혁
     * @since 2025-10-14
     */
    @PostMapping("/reply/{qnaId}")
    public ResponseEntity<ResponseDto<QnaReplyDto>> createQnaReply(@PathVariable Long qnaId,
                                                                   @AuthenticationPrincipal CustomUserPrincipal principal,
                                                                   @RequestBody QnaReplyCreateRequestDto dto) {
        return replyService.createQnaReply(qnaId, principal.creatorId(), dto);
    }
}
