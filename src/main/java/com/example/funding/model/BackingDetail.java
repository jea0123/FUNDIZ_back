package com.example.funding.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BackingDetail {
    private Long backingId;
    private Long rewardId;
    private Long price;
    private Long quantity;
}

