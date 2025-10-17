package com.example.funding.dto.response.backing.userList_detail;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MyPageBackingSaveDto {
    private Long projectId;
    private String title;
    private Long goalAmount;
    private Long currAmount;
    private LocalDateTime endDate;
    private String thumbnail;

    //후원 테이블
    private Long userId;
    private Long backingId;
    private Long amount;
    private LocalDateTime createdAt;
    private String backingStatus;

    //배송 테이블
    private String shippingStatus;

    //창작자 테이블
    private String creatorName;

    private Long rewardId;
    private String rewardName;
    private Long price;
    private LocalDateTime deliveryDate;

    // 후원상세
    private Long quantity;




}
