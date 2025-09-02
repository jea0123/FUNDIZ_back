package com.example.funding.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MyPageBackingProjectDto {
    private Long projectId;
    private String creatorid;
    private String thumbnail;
    private String title;
    private String goalAmount;
    private String currAmount;
    private LocalDate endDate;
    private String projectStatus;
}
