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
}
