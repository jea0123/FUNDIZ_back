package com.example.funding.dto.request.settlement;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class SettlementSearchCond {
    private final String q;
    private final String status;
    private final LocalDate from;
    private final LocalDate to;
}
