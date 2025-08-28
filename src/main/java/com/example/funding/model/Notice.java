package com.example.funding.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Notice {
    private long noticeId;
    private long adId;
    private String title;
    private String content;
    private long viewCnt;
    private Date createdAt;
}
