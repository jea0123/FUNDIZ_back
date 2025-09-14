package com.example.funding.dto.response.user;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPageBackingRewardDto {
    private Long rewardId;
    private String rewardName;
    private LocalDate deliveryDate;
    private MyPageBackingProjectDto backingProject;

}
