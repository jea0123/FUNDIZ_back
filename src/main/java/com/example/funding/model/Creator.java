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
    private String profileImg;
    private String bio;
    private String email;
    private String phone;
    private String bank;
    private String account;
    private String businessNum;
    private Long followerCnt;
    private String creatorType;
}
