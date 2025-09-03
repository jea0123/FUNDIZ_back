package com.example.funding.dto.response.user;

import com.example.funding.enums.BackingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MyPageBackingListDto {
    //후원한 프로젝트 목록
    
    //후원에서 가져올 데이터
    private long backingId;
    private BackingStatus backingStatus;
    private String rewardId;

    //후원상세 에서 가져올 데이터
    private long price;
    private long amount;

    //프로젝트에서 가져올 데이터
    MyPageBackingProjectDto project;

}
