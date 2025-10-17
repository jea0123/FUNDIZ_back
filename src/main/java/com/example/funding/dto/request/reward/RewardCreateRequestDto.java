package com.example.funding.dto.request.reward;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardCreateRequestDto {
    private Long projectId;
    private String rewardName;
    private Long price;
    private String rewardContent;
    private LocalDateTime deliveryDate;
    private Integer rewardCnt;
    private Character isPosting;
}
