package com.example.funding.dto.response.project;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class RecentTop10ProjectDto {
    Long projectId;
    String title;
    String creatorName;
    String thumbnail;
    Integer currAmount;
    Date endDate;
    Integer percentNow;
    Double trendScore;
}
