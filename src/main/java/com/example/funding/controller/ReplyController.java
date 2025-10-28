package com.example.funding.controller;

import com.example.funding.common.CustomUserPrincipal;
import com.example.funding.dto.ResponseDto;
import com.example.funding.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reply")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    /**
     * <p>댓글 삭제</p></p>
     *
     * @param replyId 댓글 ID
     * @param principal 인가된 사용자
     * @return 성공 시 200 OK
     * @since 2025-10-28
     */
    @DeleteMapping("/{replyId}")
    public ResponseEntity<ResponseDto<String>> deleteReply(@PathVariable Long replyId,
                                                           @AuthenticationPrincipal CustomUserPrincipal principal) {
        return replyService.deleteReply(replyId, principal.userId());
    }
}
