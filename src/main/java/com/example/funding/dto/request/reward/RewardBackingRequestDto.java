package com.example.funding.dto.request.reward;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardBackingRequestDto {
    // 리워드
    private Long rewardId;
    private String rewardName;
    private Long price;
    private String rewardContent;
    private Long quantity;

}
