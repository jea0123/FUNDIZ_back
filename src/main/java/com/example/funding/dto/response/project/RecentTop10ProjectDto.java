package com.example.funding.dto.response.project;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class RecentTop10ProjectDto {
    Long projectId;
    String title;
    String thumbnail;
    Long creatorId;
    String creatorName;
    Integer currAmount;
    Integer goalAmount;
    Date endDate;
    Integer percentNow;
    Double trendScore;
}
