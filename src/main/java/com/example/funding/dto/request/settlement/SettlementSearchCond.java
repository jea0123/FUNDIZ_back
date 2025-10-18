package com.example.funding.dto.request.settlement;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SettlementSearchCond(String q, String status, LocalDateTime from, LocalDateTime to) {
}
