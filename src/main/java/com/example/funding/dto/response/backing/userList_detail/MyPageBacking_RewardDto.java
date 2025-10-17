package com.example.funding.dto.response.backing.userList_detail;

import lombok.*;

import java.time.LocalDate;

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
    private LocalDate deliveryDate;

    // 후원상세
    private Long quantity;
    private Long backingId;
    private Long userId;

}
