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
    private long creatorId;
    private long userId;
    private String creatorName;
    private String creatorType;
    private long followerCnt;
    private String businessNum;
    private String profileImg;
    private String email;
    private String phone;
}
