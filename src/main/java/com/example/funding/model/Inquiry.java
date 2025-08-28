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
public class Inquiry {
    private long inqId;
    private long userId;
    private long adId;
    private String title;
    private String content;
    private Date createdAt;
    private char isCanceled;
    private String ctgr;
    private char isAnswer;
}
