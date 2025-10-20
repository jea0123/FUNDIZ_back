package com.example.funding.dto.response.creator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TotalCountsDto {
    private Long totalReviews;
    private Integer totalProjects;
    private Integer totalFollowers;
}
