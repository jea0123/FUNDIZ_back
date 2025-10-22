package com.example.funding.controller;

import com.example.funding.common.CustomUserPrincipal;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.creator.QnaReplyCreateRequestDto;
import com.example.funding.dto.response.cs.QnaReplyDto;
import com.example.funding.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/qna")
@RequiredArgsConstructor
public class QnaController {

    private final ReplyService replyService;

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
