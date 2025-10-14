package com.example.funding.dto.response.cs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class QnaReplyDto {
    private Long replyId;
    private Long qnaId;
    private String content;
    private LocalDateTime createdAt;
    private Long creatorId;
}
