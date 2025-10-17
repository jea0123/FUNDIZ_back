package com.example.funding.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Creator {
    private Long creatorId;
    private Long userId;
    private String creatorName;
    private String creatorType;
    private Long followerCnt;
    private String businessNum;
    private String profileImg;
    private String email;
    private String phone;
    private String account;
    private String bank;
}
