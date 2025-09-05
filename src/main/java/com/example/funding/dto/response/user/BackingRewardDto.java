package com.example.funding.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BackingRewardDto {
    private long rewardId;
    private String rewardName;
    private Date deliveryDate;
    private BackingProjectDto BackingProject;

}
