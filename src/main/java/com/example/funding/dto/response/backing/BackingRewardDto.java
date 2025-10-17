package com.example.funding.dto.response.backing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BackingRewardDto {
    private Long rewardId;
    private String rewardName;
    private Long price;
    private Long quantity;
    private LocalDateTime deliveryDate;
}
