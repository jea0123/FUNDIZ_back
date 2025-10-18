package com.example.funding.dto.request.cs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class IqrAddRequestDto {
    private Long userId;
    private Long adId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private Character isCanceled;
    private String ctgr;
    private Character isAnswer;
}
