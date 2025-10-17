package com.example.funding.dto.response.creator;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreatorProjectListDto {
    private Long projectId;
    private String title;
    private String projectStatus;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer goalAmount;
    private Integer currAmount;
    private Integer backerCnt;
    private String ctgrName;
    private String subctgrName;

    //계산 필드
    private Integer percentNow;

    //VERIFYING
    private LocalDateTime requestedAt;

    //프론트
    private Integer newsCount;
    private LocalDateTime lastNewsAt;
    private Integer reviewNewCount; // 새 후기 수
    private Integer reviewPendingCount; // 미답글 수
    private LocalDateTime lastReviewAt;
}
