package com.example.funding.dto.response.project;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ReviewDto {
    private Long cmId;
    private String cmContent;
    private LocalDateTime createdAt;
    private String code;
    private Integer rating;
    private String nickname;
    private String profileImg;
    private Long replyCnt;
}
