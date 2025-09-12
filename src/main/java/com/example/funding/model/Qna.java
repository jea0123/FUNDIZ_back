package com.example.funding.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Qna {
    private Long qnaId;
    private Long projectId;
    private Long userId;
    private Long creatorId;
    private String title;
    private String content;
    private LocalDate createdAt;
}