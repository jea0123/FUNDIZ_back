package com.example.funding.dto.response.backing.userList_detail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyPageBackingListDto {
    //프로젝트 테이블
    private String title;
    private Long goalAmount;
    private Long currAmount;
    private LocalDate endDate;
    private String thumbnail;

    //리워드 테이블
    private List<MyPageBackingList_reward> mpBackingList_rw;

    //후원상세 테이블
    private List<MyPageBackingList_backingDetail> mpBackingList_bd;

    //후원 테이블
    private Long amount;
    private LocalDate createdAt;
    private String backingStatus;

    //배송 테이블
    private String shippingStatus;

}
