package com.example.funding.dto.response.creator;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class DailyCountDto {
    private LocalDateTime createdAt;
    private Long count;
}
