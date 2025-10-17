package com.example.funding.dto.response.backing.userList_detail;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPageBacking_RewardDto {
    private Long projectId;
    private String rewardName;
    private Long price;
    private LocalDateTime deliveryDate;

    // 후원상세
    private Long quantity;
    private Long backingId;
    private Long userId;

}
