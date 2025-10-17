package com.example.funding.dto.response.user;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPageBackingProjectDto {
    private Long projectId;
    private String thumbnail;
    private String title;
    private Long goalAmount;
    private Long currAmount;
    private LocalDateTime endDate;
    private String projectStatus;
}
