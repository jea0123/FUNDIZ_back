package com.example.funding.dto.response.project;

import com.example.funding.model.News;
import com.example.funding.model.Reward;
import com.example.funding.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ProjectDetailDto {
    //프로젝트
    private Long projectId;
    private Long creatorId;
    private String title;
    private Integer goalAmount;
    private Integer currAmount;
    private Date startDate;
    private Date endDate;
    private String content;
    private String thumbnail;
    private String projectStatus;
    private Integer backerCnt;
    private Integer likeCnt;
    private Integer viewCnt;
    private Integer percentNow; //달성률
    private Date paymentDate; //결제일

    //창작자
    private String creatorName;
    private Long followerCnt;
    private String profileImg;
    private Integer projectCnt;

    //서브카테고리
    private SubcategoryDto subcategory;
    //태그
    private List<Tag> tagList;
    //리워드
    private List<Reward> rewardList;
    //새소식
    private List<News> newsList;

    //결제일 = 종료날짜 + 1
    public Date getPaymentDate() {
        if (endDate == null) return null;
        return Date.from(endDate.toInstant().plus(1, ChronoUnit.DAYS));
    }
}
