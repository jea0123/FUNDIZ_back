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
    private Long inqId;
    private Long userId;
    private Long adId;
    private String title;
    private String content;
    private Date createdAt;
    private Character isCanceled;
    private String ctgr;
    private Character isAnswer;
}
