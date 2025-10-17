package com.example.funding.dto.response.creator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CreatorProjectSummaryDto {
    private Long projectId;
    private String title;
    private LocalDateTime endDate;
    private String projectStatus;
}
