package com.example.funding.dto.response.project;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecentTop10ProjectDto {
    Long projectId;
    String title;
    String creatorName;
    String thumbnail;
    Integer currAmount;
    Integer percentNow;
    Double trendScore;
}
