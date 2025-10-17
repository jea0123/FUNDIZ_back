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
public class Community {
    private Long cmId;
    private Long userId;
    private Long projectId;
    private String cmContent;
    private Integer rating;
    private LocalDateTime createdAt;
    private String code;
}
