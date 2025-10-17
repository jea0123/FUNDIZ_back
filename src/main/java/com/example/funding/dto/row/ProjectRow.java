package com.example.funding.dto.row;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProjectRow {
    private Long projectId;
    private Long creatorId;
    private Long subctgrId;

    //프로젝트
    private String title;
    private Integer goalAmount;
    private Integer currAmount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String content;
    private String thumbnail;
    private String projectStatus;
    private Integer backerCnt;
    private Integer likeCnt;
    private Integer viewCnt;

    //창작자
    private String creatorName;
    private Long followerCnt;
    private String profileImg;

    //카테고리
    private String ctgrName;
    private String subctgrName;
}
