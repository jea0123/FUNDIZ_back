package com.example.funding.dto.response.creator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CreatorRewardDto {
    private Long rewardId;
    private Long projectId;
    private String rewardName;
    private Long price;
    private String rewardContent;
    private LocalDate deliveryDate;
    private Integer rewardCnt;
    private Character isPosting;
}
