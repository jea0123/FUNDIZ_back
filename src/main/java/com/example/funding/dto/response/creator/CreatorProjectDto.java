package com.example.funding.dto.response.creator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CreatorProjectDto {
    private Long projectId;
    private String title;
    private String thumbnail;
    private Long currAmount;
    private Long goalAmount;
    private Long backerCnt;
    private String projectStatus;
    private LocalDateTime endDate;
    private Boolean isSuccess;
}
