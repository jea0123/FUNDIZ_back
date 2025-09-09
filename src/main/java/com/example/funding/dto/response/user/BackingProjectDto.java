package com.example.funding.dto.response.user;

import lombok.*;

import java.util.Date;

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
    private Date endDate;
    private String projectStatus;
}
