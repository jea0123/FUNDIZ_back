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
public class MyPageLikedDto {
    //좋아요 테이블
    private Long userId;
    private Long projectId;
    private LocalDateTime createdAt;

    //창작자 테이블

    private Long creatorId;
    private String creatorName;

    //프로젝트 테이블
    private String title;
    private Long goalAmount;
    private Long currAmount;
    private String thumbnail;
    private LocalDateTime endDate;
}
