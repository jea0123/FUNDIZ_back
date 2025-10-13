package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.creator.CreatorSettlementDto;
import com.example.funding.dto.row.SettlementSummary;
import com.example.funding.exception.CreatorNotFountException;
import com.example.funding.mapper.CreatorMapper;
import com.example.funding.mapper.SettlementMapper;
import com.example.funding.model.Settlement;
import com.example.funding.service.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SettlementServiceImpl implements SettlementService {

    private final CreatorMapper creatorMapper;
    private final SettlementMapper settlementMapper;

    @Override
    public ResponseEntity<ResponseDto<CreatorSettlementDto>> getSettlementByCreatorId(Long creatorId) {
        if (creatorMapper.existsCreator(creatorId) == 0) {
            throw new CreatorNotFountException();
        }
        List<Settlement> settlement = settlementMapper.getByCreatorId(creatorId);
        SettlementSummary settlementSummary = settlementMapper.getSettlementSummaryByCreatorId(creatorId);
        CreatorSettlementDto dtos = CreatorSettlementDto.builder()
                .settlement(settlement)
                .settlementSummary(settlementSummary)
                .build();
        return ResponseEntity.ok(ResponseDto.success(200, "크리에이터 정산 정보 조회 성공", dtos));
    }
}
