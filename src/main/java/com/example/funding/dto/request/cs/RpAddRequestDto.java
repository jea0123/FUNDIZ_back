    package com.example.funding.dto.request.cs;

    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Getter;
    import lombok.Setter;

    import java.time.LocalDateTime;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public class RpAddRequestDto {
        private Long userId;
        private Long target;
        private String reason;
        private LocalDateTime reportDate;
        private String reportStatus;
        private String reportType;
    }
