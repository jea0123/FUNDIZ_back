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
public class MyPageLikedDto {
    //좋아요 테이블
    private Long projectId;
    private Date createdAt;

    //창작자 테이블
    private String creatorName;

    //프로젝트 테이블
    private String title;
    private Long goalAmount;
    private Long currAmount;
    private String thumbnail;
    private Date endDate;
}
