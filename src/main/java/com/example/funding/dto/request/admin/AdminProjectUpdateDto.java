package com.example.funding.dto.request.admin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminProjectUpdateDto {
    private Long projectId;
    private Long subctgrId;
    private String title;
    private String thumbnail;
    private String projectStatus;
}
