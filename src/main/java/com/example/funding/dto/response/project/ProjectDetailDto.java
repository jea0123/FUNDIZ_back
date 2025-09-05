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
    private Integer viewCnt;
    //달성률
    private Integer percentNow;
    //결제일
    private Date paymentDate;

    //창작자 관련
    private String creatorName;
    private Long followerCnt;
    private String profileImg;
    private Integer projectCnt;

    private SubcategoryDto subcategory;

    private List<Tag> tagList;
    private List<Reward> rewardList;
    private List<News> newsList;

    //결제일 = 종료날짜 + 1
    public Date getPaymentDate() {
        if (endDate == null) return null;
        return Date.from(endDate.toInstant().plus(1, ChronoUnit.DAYS));
    }
}
