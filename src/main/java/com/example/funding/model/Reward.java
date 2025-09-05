package com.example.funding.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Reward {
    private Long rewardId;
    private Long projectId;
    private String rewardName;
    private Integer price;
    private String rewardContent;
    private Date deliveryDate;
    private Integer rewardCnt;
    private Date createdAt;
    private Character isPosting;
    private Integer remain;
}
