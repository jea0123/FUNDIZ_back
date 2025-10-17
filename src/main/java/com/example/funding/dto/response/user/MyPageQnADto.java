package com.example.funding.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MyPageQnADto {
    // qna 테이블
    private Long qnAId;
    private Long projectId;
    private Long userId;
    private Long creatorId;
    private String title;
    private LocalDateTime date;

    // 창작자 테이블
    private String creatorName;
}
