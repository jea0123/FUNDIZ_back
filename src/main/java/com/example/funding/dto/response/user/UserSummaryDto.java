package com.example.funding.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserSummaryDto {
    private Long userId;
    private Long backingCount;
    private Long likedCount;
    private Long followCreatorCount;
    private Long notificationCount;
}
