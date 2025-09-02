package com.example.funding.dto.response.user;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MyPageBackingDto {
    //후원한 프로젝트 목록
    private long backingId;
    // ENUM 타입으로 전환 필요
    private String backingStatus;
    private String rewardId;
}
