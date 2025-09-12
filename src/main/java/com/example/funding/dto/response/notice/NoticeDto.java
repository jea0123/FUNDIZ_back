package com.example.funding.dto.response.notice;

import com.example.funding.model.Files;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class NoticeDto {
    private Long noticeId;
    private Long adId;
    private String title;
    private String content;
    private Long viewCnt;
    private LocalDate createdAt;

    private List<Files> filesList;
}
