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
    private int price;
    private String rewardContent;
    private Date deliveryDate;
    private int rewardCnt;
    private Date createdAt;
    private char isPosting;
    private int remain;
}
