package com.example.funding.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Reward {
    private Long rewardId;
    private Long projectId;
    private String rewardName;
    private Long price;
    private String rewardContent;
    private Integer rewardCnt;
    private Integer remain;
    private Character isPosting;
    private LocalDateTime deliveryDate;
    private LocalDateTime createdAt;
}
