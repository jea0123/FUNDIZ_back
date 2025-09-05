package com.example.funding.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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
    private Date date;

    // 창작자 테이블
    private String creatorName;
}
