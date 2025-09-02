package com.example.funding.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BackingDetail {
    private Long backingId;
    private Long rewardId;
    private Long price;
    private Long quantity;
}

