package com.example.funding.dto.response.user;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPageBackingRewardDto {
    private Long rewardId;
    private String rewardName;
    private LocalDateTime deliveryDate;
    private MyPageBackingProjectDto backingProject;

}
