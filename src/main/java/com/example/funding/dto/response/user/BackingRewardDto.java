package com.example.funding.dto.response.user;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BackingRewardDto {
    private Long rewardId;
    private String rewardName;
    private LocalDate deliveryDate;
    private BackingProjectDto backingProject;

}
