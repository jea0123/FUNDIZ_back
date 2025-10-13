package com.example.funding.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
