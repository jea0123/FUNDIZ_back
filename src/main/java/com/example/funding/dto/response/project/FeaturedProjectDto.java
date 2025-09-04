package com.example.funding.dto.response.project;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeaturedProjectDto {
    Long projectId;
    String title;
    String creatorName;
    String thumbnail;

    Integer percentNow;
    Integer goalAmount;

    Double score;
}
