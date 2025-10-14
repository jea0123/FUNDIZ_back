package com.example.funding.dto.response.cs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class InquiryReplyDto {
    private Long replyId;
    private Long inqId;
    private Long adId;
    private String content;
    private LocalDateTime createdAt;
}
