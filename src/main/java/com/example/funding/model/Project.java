package com.example.funding.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Project {
    private Long projectId;
    private Long creatorId;
    private Long subctgrId;
    private String title;
    private Integer goalAmount;
    private Integer currAmount;
    private Date startDate;
    private Date endDate;
    private String content;
    private String thumbnail;
    private Date createdAt;
    private Date updatedAt;
    private String projectStatus;
    private Integer backerCnt;
    private Integer likeCnt;
    private Integer viewCnt;
    private Character isReqReview;
    private String rejectedReason;
}
