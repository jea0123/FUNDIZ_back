package com.example.funding.dto.response.cs;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class NoticeDetailDto {
    private Long noticeId;
    private Long adId;
    private String title;
    private String content;
    private Long viewCnt;
    private LocalDateTime createdAt;

}
