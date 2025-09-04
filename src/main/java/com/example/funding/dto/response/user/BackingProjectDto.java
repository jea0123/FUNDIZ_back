package com.example.funding.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BackingProjectDto {
    private Long projectId;
    private String thumbnail;
    private String title;
    private String goalAmount;
    private String currAmount;
    private Date endDate;
    private String projectStatus;
}
