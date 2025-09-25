    package com.example.funding.dto.request.cs;

    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Getter;
    import lombok.Setter;

    import java.time.LocalDate;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public class NoticeUpdateRequestDto {
        private Long noticeId;
        private Long adId;
        private String title;
        private String content;
        private Long viewCnt;
        private LocalDate createdAt;

    }
