package com.example.funding.dto.response.project;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class RecentTop10ProjectDto {
    private Long projectId;
    private String title;
    private String thumbnail;
    private Long creatorId;
    private String creatorName;
    private Integer currAmount;
    private Integer goalAmount;
    private LocalDate endDate;
    private Integer percentNow;
    private Double trendScore;
}
