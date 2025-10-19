package com.example.funding.dto.response.admin;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AdminProjectListDto {
    private Long projectId;
    private String title;
    private Integer goalAmount;
    private Integer currAmount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String projectStatus;
    private Integer backerCnt;
    private Integer likeCnt;
    private Integer viewCnt;

    private String subctgrName;
    private String ctgrName;

    private String creatorName;

    //계산 필드
    private Integer percentNow;
}
