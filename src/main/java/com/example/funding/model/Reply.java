package com.example.funding.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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
    private char isSecret;
    private Date createdAt;
    private Date deletedAt;
    private String code;
}
