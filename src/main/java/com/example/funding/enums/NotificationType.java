package com.example.funding.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    FUNDING_UPCOMING("펀딩이 승인되었습니다! '{title}' 펀딩이 곧 시작됩니다."),
    FUNDING_REJECTED("안타깝게도 '{title}' 펀딩이 승인되지 않았습니다."),
    FUNDING_OPEN("새로운 펀딩이 시작되었습니다! '{title}'"),
    FUNDING_SUCCESS("축하합니다! '{title}' 펀딩이 성공적으로 완료되었습니다."),
    FUNDING_FAILURE("안타깝게도 '{title}' 펀딩이 목표 금액에 도달하지 못했습니다."),
    FUNDING_SETTLED("'{title}' 펀딩이 성공적으로 정산되었습니다."),
    SHIPPING_SENT("좋은 소식입니다! '{title}' 리워드가 발송되었습니다."),
    SHIPPING_DELIVERED("'{title}' 리워드가 성공적으로 배송되었습니다."),
    BACKING_SUCCESS("펀딩 참여가 성공적으로 완료되었습니다: '{title}'"),
    BACKING_FAIL("펀딩 참여에 실패했습니다: '{title}'"),
    PAYMENT_SUCCESS("결제가 성공적으로 완료되었습니다: '{title}'"),
    PAYMENT_FAIL("결제에 실패했습니다: '{title}'"),
    NEW_FOLLOWER("새로운 팔로워가 생겼습니다: '{title}'"),
    COMMUNITY_REPLY("작성하신 커뮤니티 글에 새로운 댓글이 달렸습니다: '{title}'"),
    QNA_REPLY("질문에 새로운 답변이 등록되었습니다: '{title}'"),
    QNA_NEW("새로운 질문이 등록되었습니다: '{title}'"),
    INQUIRY_ANSWERED("문의하신 내용에 답변이 등록되었습니다: '{title}'"),
    REPORT_RECEIVED("신고가 접수되었습니다. 확인 후 처리 예정입니다: '{title}'"),
    REPORT_RESOLVED("신고하신 내용이 처리되었습니다: '{title}'"),
    REFUND_COMPLETED("환불이 성공적으로 완료되었습니다: '{title}'"),
    REWARD_OUT_OF_STOCK("'{title}' 리워드가 품절되었습니다."),
    DEFAULT("새로운 알림이 있습니다: '{title}'");

    private final String template;

    public String render(String title) {
        return template.replace("{title}", title == null ? "" : title);
    }
}
