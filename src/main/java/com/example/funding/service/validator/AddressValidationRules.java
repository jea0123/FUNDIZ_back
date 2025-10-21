package com.example.funding.service.validator;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@UtilityClass
public class AddressValidationRules {
    //주소
    public static final int MAX_ADDR_NAME_LEN = 30;
    public static final int MAX_ROAD_ADDR_LEN = 100;
    public static final int MAX_DETAIL_ADDR_LEN = 100;
    public static final Pattern POSTAL_PATTERN = Pattern.compile("^[0-9]{5}$");
    public static final Pattern PHONE_PATTERN = Pattern.compile("^(?:(?:01[0-9])|(?:02|0[3-6][1-5]|070))[-]?[0-9]{3,4}[-]?[0-9]{4}$");

    // 배송
    public static final Pattern TRACKING_NUM_PATTERN = Pattern.compile("^[0-9]{10,14}$");

    // 배송상태 전환 규칙
    public static final Map<String, List<String>> SHIPPING_ALLOWED_TRANSITIONS = Map.of(
            "PENDING", List.of("READY"),
            "READY", List.of("SHIPPED"),
            "SHIPPED", List.of("DELIVERED", "FAILED"),
            "DELIVERED", List.of("CANCELED"),
            "CANCELED", List.of(),
            "FAILED", List.of()
    );

    // 배송 상태 라벨
    public static final Map<String, String> SHIPPING_STATUS_LABEL = Map.of(
            "PENDING", "후원 완료",
            "READY", "상품 준비 중",
            "SHIPPED", "배송 시작",
            "DELIVERED", "배송 완료",
            "CANCELED", "취소",
            "FAILED", "배송 실패"
    );

    // 공백 검사
    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
