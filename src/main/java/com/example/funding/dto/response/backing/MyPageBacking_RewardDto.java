package com.example.funding.dto.response.backing;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPageBacking_RewardDto {
    private Long projectId;
    private Long rewardId;
    private String rewardName;
    private Long price;
    private LocalDateTime deliveryDate;

    // 후원상세
    private Long quantity;
    private Long backingId;
    private Long userId;

}
