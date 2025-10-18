package com.example.funding.dto.request.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class QnaAddRequestDto {
    private Long projectId;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;
}
