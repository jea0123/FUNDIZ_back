package com.example.funding.dto.response.admin;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AdminProjectListDto {
    private Long projectId;
    private String title;
    private String creatorName;
    private String projectStatus;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer goalAmount;
    private Integer currAmount;
    private Integer backerCnt;
    private LocalDateTime updatedAt;

    //계산 필드
    private Integer percentNow;
}
