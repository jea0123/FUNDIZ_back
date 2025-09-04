package com.example.funding.dto.response.project;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class FeaturedProjectDto {
    Long projectId;
    String title;
    String creatorName;
    String thumbnail;
    Date endDate;
    Integer percentNow;
    Integer goalAmount;

    Double score;
}
