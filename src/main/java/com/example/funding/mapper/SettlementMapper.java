package com.example.funding.mapper;

import com.example.funding.dto.request.admin.SettlementPaidRequestDto;
import com.example.funding.dto.row.SettlementSummary;
import com.example.funding.model.Settlement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SettlementMapper {
    List<Settlement> getByCreatorId(Long creatorId);

    SettlementSummary getSettlementSummaryByCreatorId(Long creatorId);

    long getTotalAmountCreatorId(Long creatorId);

    void updateSettlementPaid(SettlementPaidRequestDto dto);

    int bulkInsertSettlementWaiting(
            @Param("feeRate") double feeRate,
            @Param("statusColumn") String statusColumn
    );

    int existsByProjectId(Long projectId);

    int isPaid(@Param("projectId") Long projectId, @Param("settlementId") Long settlementId);
}
