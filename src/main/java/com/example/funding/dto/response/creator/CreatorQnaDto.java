package com.example.funding.dto.response.creator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CreatorQnaDto {
    private Long qnaId;
    private Long projectId;
    private Long userId;
    private Long creatorId;
    private String content;
    private LocalDateTime createdAt;
    private String title;
}