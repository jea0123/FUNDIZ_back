package com.example.funding.dto.response.shipping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CreatorShippingBackerList {
    //유저
    private Long userId;
    private String email;
    private String nickname;

    //창작자
    private Long creatorId;

    //리워드
    private String rewardName;

    //배송지
    private String recipient;
    private String postalCode;
    private String roadAddr;
    private String detailAddr;
    private String recipientPhone;

    //배송
    private String shippingStatus;
    private String trackingNum;
    private LocalDate shippedAt;
    private LocalDate deliveredAt;

    //후원상세
    private Long quantity;

    // 프로젝트
    private Long projectId;
    private String title;

}
