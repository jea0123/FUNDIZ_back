package com.example.funding.dto.response.project;

import com.example.funding.model.News;
import com.example.funding.model.Reward;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ProjectDetailDto {
    private Long projectId;
    private String title;
    private int goalAmount;
    private int currAmount;
    private Date startDate;
    private Date endDate;
    private String content;
    private String thumbnail;
    private String projectStatus;
    private int backerCnt;
    private int viewCnt;

    private Long creatorId;
    private String ctrgName;
    private Long subctrgId;
    private String subctrgName;

    private List<String> tagList;

    private List<Reward> rewardList;

    private List<News> newsList;

    private List<CommunityDto> communityList;
}
