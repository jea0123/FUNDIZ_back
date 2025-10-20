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
public class CreatorSummaryDto {
    private CreatorRow creator;
    private Stats stats;
    private Boolean isFollowed;
    private Long followerCount;
    private LocalDateTime lastLogin;

    @Getter
    @Setter
    public static class CreatorRow {
        private Long creatorId;
        private String creatorName;
        private String profileImg;
    }

    @Getter
    @Setter
    public static class Stats {
        private Long projectCount;
        private Long totalBackers;
        private Long totalAmount;
    }
}
