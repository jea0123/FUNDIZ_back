package com.example.funding.dto.response.reward;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class OrderRewardDto {
    private Long rewardId;
    private String rewardName;
    private Long price;
    private Long quantity;
    private Date deliveryDate;
}
