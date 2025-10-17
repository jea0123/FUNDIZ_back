package com.example.funding.dto.request.settlement;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SettlementSearchCond {
    private final String q;
    private final String status;
    private final LocalDateTime from;
    private final LocalDateTime to;
}
