package com.example.funding.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Backing {
    private Long backingId;
    private Long userId;
    private Long payInfoId;
    private Long amount;
    private LocalDateTime createdAt;
    private String backingStatus;
}
