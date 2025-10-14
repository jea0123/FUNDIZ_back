package com.example.funding.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Reply {
    private Long replyId;
    private Long userId;
    private Long qnaId;
    private Long cmId;
    private Long inqId;
    private String content;
    private Character isSecret;
    private LocalDate createdAt;
    private LocalDate deletedAt;
    private String code;
    private Long creatorId;
}
