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
    public class RpAddRequestDto {
        private Long userId;
        private Long target;
        private String reason;
        private LocalDate reportDate;
        private String reportStatus;
        private String reportType;
    }
