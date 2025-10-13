package com.example.funding.dto.response.creator;

import com.example.funding.dto.row.SettlementSummary;
import com.example.funding.model.Settlement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CreatorSettlementDto {
    private List<Settlement> settlement;
    private SettlementSummary settlementSummary;
}
