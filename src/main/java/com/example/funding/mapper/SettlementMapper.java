package com.example.funding.mapper;

import com.example.funding.dto.row.SettlementSummary;
import com.example.funding.model.Settlement;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SettlementMapper {
    List<Settlement> getByCreatorId(Long creatorId);
    SettlementSummary getSettlementSummaryByCreatorId(Long creatorId);
    long getTotalAmountCreatorId(Long creatorId);
}
