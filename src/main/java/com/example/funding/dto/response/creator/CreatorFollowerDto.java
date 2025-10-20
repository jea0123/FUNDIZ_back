package com.example.funding.dto.response.creator;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatorFollowerDto {
    private Long userId;
    private String nickname;
    private String userProfileImg;
    private LocalDate followDate;
    private boolean isCreator;

    // 팔로워가 크리에이터인 경우만 값 존재
    private Long creatorId;
    private String creatorName;
    private String creatorProfileImg;

    private boolean canFollow;
    private boolean isFollowing;
}
