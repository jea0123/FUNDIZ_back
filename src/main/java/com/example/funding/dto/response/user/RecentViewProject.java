package com.example.funding.dto.response.user;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RecentViewProject {
    private Long projectId;
    private String creatorName;
    private String title;
    private String thumbnail;
    private Integer percentNow;
    private Integer currAmount;
    private LocalDate endDate;
}
