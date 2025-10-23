package com.example.funding.dto.response.creator;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreatorProjectListDto {
    private Long projectId;
    private String title;
    private Integer goalAmount;
    private Integer currAmount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String thumbnail;
    private String projectStatus;
    private Integer backerCnt;
    private Integer likeCnt;
    private Integer viewCnt;
    private LocalDateTime requestedAt;
    private String rejectedReason;

    private String subctgrName;
    private String ctgrName;

    //계산 필드
    private Integer percentNow;

    //프론트
    private Integer newsCount; // 새 새소식 수
    private LocalDateTime lastNewsAt; // 마지막 작성일
    private Integer reviewNewCount; // 새 후기 수
    private Integer reviewPendingCount; // 미답글 수
    private LocalDateTime lastReviewAt; // 마지막 작성일
}
