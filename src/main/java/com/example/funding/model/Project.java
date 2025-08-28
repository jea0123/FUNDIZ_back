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
    private Long subctrgId;
    private String title;
    private int goalAmount;
    private int currAmount;
    private Date startDate;
    private Date endDate;
    private String content;
    private String thumbnail;
    private Date createdAt;
    private Date updatedAt;
    private String projectStatus;
    private int backerCnt;
    private int likeCnt;
    private int viewCnt;
    private char isReqReview;
    private String rejectedReason;
}
