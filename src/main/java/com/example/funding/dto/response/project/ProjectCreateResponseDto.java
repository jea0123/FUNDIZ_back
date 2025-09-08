package com.example.funding.dto.response.project;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectCreateResponseDto {
    private final Long projectId;
    private final String projectStatus;
}
