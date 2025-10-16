package com.example.funding.dto.response.backing.userList_detail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyPageBackingList_reward {
    private String rewardName;
    private Long price;
    private String rewardContent;

}
