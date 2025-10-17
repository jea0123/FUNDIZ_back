package com.example.funding.dto.response.backing.userList_detail;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPageBackingDetailSaveDto {
    private Long backingId;
    private Long userId;
    private Long amount;
    private LocalDateTime createdAt;
    private String backingStatus;

    //결제
    private String method;
    private String cardCompany;

    //배송
    private String shippingStatus;
    private String trackingNum;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;

    //프로젝트
    private String title;
    private String thumbnail;

    //주소
    private String addrName;
    private String recipient;
    private String postalCode;
    private String roadAddr;
    private String detailAddr;
    private String recipientPhone;

    //창작자
    private String creatorName;

    //리워드 리스트
    private Long projectId;
    private Long rewardId;
    private String rewardName;
    private Long price;
    private LocalDateTime deliveryDate;

    // 후원상세
    private Long quantity;

}
