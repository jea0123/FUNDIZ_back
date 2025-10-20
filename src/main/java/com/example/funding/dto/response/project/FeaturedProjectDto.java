package com.example.funding.dto.response.project;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FeaturedProjectDto {
    private Long projectId;
    private String title;
    private Long creatorId;
    private String creatorName;
    private String thumbnail;
    private LocalDateTime endDate;
    private Integer percentNow;
    private Integer currAmount;

    private Double score;
}
