package com.example.funding.dto.response.project;

import com.example.funding.model.News;
import com.example.funding.model.Reward;
import com.example.funding.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ProjectDetailDto {
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
    private String contentBlocks;
    private String thumbnail;
    private String projectStatus;
    private Integer backerCnt;
    private Integer likeCnt;
    private Integer viewCnt;

    //계산 필드
    private Integer percentNow;
    private Integer projectCnt;
    private LocalDateTime paymentDate;

    //창작자
    private String creatorName;
    private Long followerCnt;
    private String profileImg;

    //카테고리
    private String ctgrName;
    private String subctgrName;
    //태그
    private List<Tag> tagList;
    //리워드
    private List<Reward> rewardList;
    //새소식
    private List<News> newsList;
}
