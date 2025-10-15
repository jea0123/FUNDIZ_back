package com.example.funding.dto.request.reward;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardBackingRequestDto {
    // 리워드
    private String rewardName;
    private String price;

    // 후원상세
    private Long backingPrice;
    private Long quantity;
}
