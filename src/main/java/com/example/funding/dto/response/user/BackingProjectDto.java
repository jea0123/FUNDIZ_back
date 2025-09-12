package com.example.funding.dto.response.user;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BackingProjectDto {
    private Long projectId;
    private String thumbnail;
    private String title;
    private Long goalAmount;
    private Long currAmount;
    private LocalDate endDate;
    private String projectStatus;
}
