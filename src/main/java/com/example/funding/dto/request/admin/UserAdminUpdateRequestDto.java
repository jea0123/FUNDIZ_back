    package com.example.funding.dto.request.admin;

    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Getter;
    import lombok.Setter;

    import java.time.LocalDate;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public class UserAdminUpdateRequestDto {
        private Long userId;
        private String nickname;
        private Character isSuspended;
        private String reason;
        private LocalDate suspendedAt;
        private LocalDate releasedAt;
    }
