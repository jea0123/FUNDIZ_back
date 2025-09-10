package com.example.funding.dto.request.reward;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RewardCreateRequestDto {
    private String rewardName;
    private Integer price;
    private String rewardContent;
    private Date deliveryDate;
    private Integer rewardCnt;
    private Character isPosting;
}
