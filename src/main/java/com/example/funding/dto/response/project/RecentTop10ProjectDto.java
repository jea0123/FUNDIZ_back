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

    Integer percentNow;
    Integer currAmount;
    Integer goalAmount;
    Long amount24h;

    Integer likeCnt;
    Integer viewCnt;
    Double trendScore;
}
