package com.example.funding.dto.response.user;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BackingRewardDto {
    private Long rewardId;
    private String rewardName;
    private Date deliveryDate;
    private BackingProjectDto backingProject;

}
