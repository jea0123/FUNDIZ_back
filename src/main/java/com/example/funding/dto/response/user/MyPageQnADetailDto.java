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
public class MyPageQnADetailDto {

    // qna 테이블
    private Long qnaId;
    private Long projectId;
    private Long userId;
    private Long creatorId;
    private String title;
    private LocalDateTime createdAt;
    private String content;

    //프로젝트 테이블
    private String projectTitle;

    // 창작자 테이블
    private String creatorName;

    //유저 테이블
    private String nickName;


}
