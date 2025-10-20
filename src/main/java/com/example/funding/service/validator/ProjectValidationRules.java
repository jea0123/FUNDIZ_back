package com.example.funding.service.validator;

import lombok.experimental.UtilityClass;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;

@UtilityClass
public class ProjectValidationRules {
    // 기본 필드
    public static final int MIN_TITLE_LEN = 2;
    public static final int MAX_TITLE_LEN = 255;
    public static final int MIN_CONTENT_LEN = 30;
    public static final int MAX_CONTENT_LEN = 3000;
    public static final int MIN_GOAL_AMOUNT = 10_000;
    public static final int MAX_GOAL_AMOUNT = Integer.MAX_VALUE;
    public static final int MIN_DAYS = 7;
    public static final int MAX_DAYS = 60;
    public static final int MIN_START_LEAD_DAYS = 7; // 리드타임: 오늘+최소 n일 이후 시작

    // 대표이미지
    public static final Set<String> ALLOWED_IMAGE_TYPES = Set.of("image/jpeg", "image/png");
    public static final int MAX_THUMBNAIL_LEN = 500;

    // 태그
    public static final int MAX_TAGS = 3;
    public static final int MIN_TAG_LEN = 2;
    public static final int MAX_TAG_LEN = 15;
    public static final Pattern TAG_PATTERN = Pattern.compile("^[0-9A-Za-z가-힣 _-]+$");

    // 리워드
    public static final int MAX_REWARD_NAME_LEN = 255;
    public static final int MAX_REWARD_CONTENT_LEN = 255;
    public static final int MIN_REWARD_PRICE = 1_000;
    public static final int MAX_REWARD_PRICE = 30_000_000;

    // helpers
    /** 두 날짜 사이의 포함 일수(끝 미포함) */
    public static long daysInclusive(LocalDateTime start, LocalDateTime end) {
        Objects.requireNonNull(start, "start");
        Objects.requireNonNull(end, "end");
        long d = ChronoUnit.DAYS.between(start, end);
        if (d < 0) throw new IllegalArgumentException("시작일은 종료일 이전이어야 합니다.");
        return d + 1;
    }

    /** 태그 정규화: trim → 공백 압축 → NFC (표시/저장용) */
    public static String normTagDisplay(String s) {
        if (s == null) return "";
        String n = s.trim().replaceAll("\\s+", " ");
        return Normalizer.normalize(n, Normalizer.Form.NFC);
    }

    /** 태그 키: trim → 공백 압축 → NFC -> Lower (중복판정/검색/고유키용) */
    public static String normTagKey(String s) {
        if (s == null) return "";
        String n = s.trim().replaceAll("\\s+", " ");
        n = Normalizer.normalize(n, Normalizer.Form.NFC);
        return n.toLowerCase(Locale.ROOT);
    }

    /** 리워드명 정규화 */
    public static String normRewardName(String s) {
        if (s == null) return "";
        String n = s.trim().replaceAll("\\s+", " ");
        n = Normalizer.normalize(n, Normalizer.Form.NFC);
        return n.toLowerCase(Locale.ROOT);
    }

    /** null-safe */
    public static String nvl(String s) { return s == null ? "" : s; }
}
