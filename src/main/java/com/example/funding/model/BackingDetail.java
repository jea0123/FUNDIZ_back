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
    private long backingId;
    private long rewardId;
    private long price;
    private long quantity;
}

