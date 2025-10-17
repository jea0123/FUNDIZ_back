package com.example.funding.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Notice {
    private Long noticeId;
    private Long adId;
    private String title;
    private String content;
    private Long viewCnt;
    private LocalDateTime createdAt;
}
