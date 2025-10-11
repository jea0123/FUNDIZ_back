package com.example.funding.dto.response.creator;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreatorProjectListDto {
    private Long projectId;
    private String title;
    private String projectStatus;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer goalAmount;
    private Integer currAmount;
    private Integer backerCnt;
    private String ctgrName;
    private String subctgrName;

    //계산 필드
    private Integer percentNow;

    //VERIFYING
    private LocalDate requestedAt;

    //프론트
    private Integer newsCount;
    private LocalDate lastNewsAt;
    private Integer reviewNewCount; // 새 후기 수
    private Integer reviewPendingCount; // 미답글 수
    private LocalDate lastReviewAt;
}
