package com.example.funding.mapper;

import com.example.funding.dto.request.settlement.SettlementPaidRequestDto;
import com.example.funding.dto.response.settlement.SettlementItem;
import com.example.funding.dto.row.SettlementSummary;
import com.example.funding.model.Settlement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface SettlementMapper {
    List<Settlement> getByCreatorId(Long creatorId);

    SettlementSummary getSettlementSummaryByCreatorId(Long creatorId);

    long getTotalAmountCreatorId(Long creatorId);

    void updateSettlementStatus(SettlementPaidRequestDto dto);

    int bulkInsertSettlementWaiting(
            @Param("feeRate") double feeRate,
            @Param("statusColumn") String statusColumn
    );

    int existsByProjectId(Long projectId);

    String getStatus(@Param("projectId") Long projectId, @Param("creatorId") Long creatorId, @Param("settlementId") Long settlementId);

    int count(
            @Param("q") String q,
            @Param("status") String status,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );

    List<SettlementItem> findPage(
            @Param("q") String q,
            @Param("status") String status,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            @Param("startRow") int startRow,
            @Param("endRow") int endRow
    );

    SettlementSummary getSettlementSummary();
}
