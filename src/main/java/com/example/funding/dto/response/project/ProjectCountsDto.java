package com.example.funding.dto.response.project;

import lombok.*;

@Getter
@Setter
public class ProjectCountsDto {
    private Section community;
    private Section review;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Section {
        private Long total;
    }
}
