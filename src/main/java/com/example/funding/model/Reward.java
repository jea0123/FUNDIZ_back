package com.example.funding.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

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
    private LocalDate deliveryDate;
    private Integer rewardCnt;
    private LocalDate createdAt;
    private Character isPosting;
    private Integer remain;
}
