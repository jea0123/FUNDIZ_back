package com.example.funding.dto.request.reward;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RewardUpdateRequestDto {
    private Long rewardId;
    private Long projectId;
    private String rewardName;
    private Integer price;
    private String rewardContent;
    private LocalDate deliveryDate;
    private Integer rewardCnt;
    private Character isPosting;
}
