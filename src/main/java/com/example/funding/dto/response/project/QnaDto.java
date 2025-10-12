package com.example.funding.dto.response.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class QnaDto {
    private Long qnaId;
    private Long projectId;
    private Long userId;
    private Long creatorId;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    private String nickname;
    private String profileImg;

    private List<ReplyDto> replyList;
}