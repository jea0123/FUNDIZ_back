package com.example.funding.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String content;
    private String contentBlocks;
    private String thumbnail;
    private String businessDoc;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String projectStatus;
    private Integer backerCnt;
    private Integer likeCnt;
    private Integer viewCnt;
    private Character isVerified;
    private LocalDateTime requestedAt;
    private String rejectedReason;
}
