package com.example.funding.dto.request.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class ProjectCreateRequestDto {
    //세부카테고리,카테고리
    private Long subctgryId;
    private String subctgrName;
    private Long ctgrId;
    private String ctgrName;

    //프로젝트
    private String title;
    private String content;
    private String thumbnail;
    private Integer goalAmount;
    private Date startDate;
    private Date endDate;

    //태그
    private Long tagId;
    private String tagName;

    //리워드
    private Integer price;
    private Integer rewardCnt;
    private String rewardName;
    private String rewardContent;
    private Date deliveryDate;
    private Character isPosting;

    //창작자
    private String creatorName;
    private String profileImg;
    private String email;
    private String phone;
}
