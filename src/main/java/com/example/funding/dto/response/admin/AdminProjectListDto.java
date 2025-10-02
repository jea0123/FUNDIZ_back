package com.example.funding.dto.response.admin;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AdminProjectListDto {
    private Long projectId;
    private String title;
    private String creatorName;
    private String projectStatus;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer goalAmount;
    private Integer currAmount;
    private Integer backerCnt;
    private LocalDate updatedAt;

    //계산 필드
    private Integer percentNow;
}
