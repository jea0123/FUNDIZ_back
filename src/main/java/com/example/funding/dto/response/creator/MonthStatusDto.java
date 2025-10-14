package com.example.funding.dto.response.creator;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MonthStatusDto {
    private LocalDate date;
    private Long count;
}
