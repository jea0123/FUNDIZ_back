package com.example.funding.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Reply {
    private Long replyId;
    private Long userId;
    private Long creatorId;
    private Long qnaId;
    private Long cmId;
    private Long inqId;
    private String content;
    private Character isSecret;
    private LocalDateTime createdAt;
    private String code;
}
