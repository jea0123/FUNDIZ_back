package com.example.funding.dto.response.backing;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class BackingRewardDto {
    private Long rewardId;
    private String rewardName;
    private Long price;
    private Long quantity;
    private LocalDateTime deliveryDate;
}
