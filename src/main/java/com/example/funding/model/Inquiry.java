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
public class Inquiry {
    private Long inqId;
    private Long userId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private Character isCanceled;
    private String ctgr;
    private Character isAnswer;
}
