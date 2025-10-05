package com.example.funding.dto.response.project;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReplyDto {
    private Long replyId;
    private Long cmId;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;
}
