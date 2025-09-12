package com.example.funding.dto.request.project;

import com.example.funding.dto.request.reward.RewardCreateRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProjectCreateRequestDto {
    private Long projectId;
    private Long subctgrId;
    private Long creatorId;

    //프로젝트
    private String title;
    private String content;
    private String thumbnail;
    private Integer goalAmount;
    private Date startDate;
    private Date endDate;

    //태그
    private List<String> tagList;

    //리워드
    private List<RewardCreateRequestDto> rewardList;

    //창작자
    private String creatorName;
    private String businessNum;
    private String email;
    private String phone;
}
