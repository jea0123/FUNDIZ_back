package com.example.funding.dto.response.project;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class FeaturedProjectDto {
    private Long projectId;
    private String title;
    private String creatorName;
    private String thumbnail;
    private LocalDate endDate;
    private Integer percentNow;
    private Integer currAmount;

    private Double score;
}
