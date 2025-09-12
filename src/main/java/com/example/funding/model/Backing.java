package com.example.funding.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Backing {
    private Long backingId;
    private Long userId;
    private Long amount;
    private LocalDate createdAt;
    private String backingStatus;
}
