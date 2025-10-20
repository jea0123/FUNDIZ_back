package com.example.funding.dto.request.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserAdminUpdateRequestDto {
    private Long userId;
    private String nickname;
    private Character isSuspended;
    private String reason;
    private LocalDateTime suspendedAt;
    private LocalDateTime releasedAt;
}
