package com.example.funding.dto.response.admin;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProjectVerifyListDto {
    private Long projectId;
    private String title;
    private String creatorName;
    private Long ctgrId;
    private String ctgrName;
    private Long subctgrId;
    private String subctgrName;
    private Integer goalAmount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String thumbnail;
    private String projectStatus;
    private LocalDateTime requestedAt;
}
