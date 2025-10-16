package com.example.funding.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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
    private LocalDate startDate;
    private LocalDate endDate;
    private String content;
    private String contentBlocks;
    private String thumbnail;
    private String businessDoc;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private String projectStatus;
    private Integer backerCnt;
    private Integer likeCnt;
    private Integer viewCnt;
    private Character isVerified;
    private LocalDate requestedAt;
    private String rejectedReason;
}
