package com.example.funding.service.impl;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.settlement.SettlementSearchCond;
import com.example.funding.dto.request.settlement.SettlementPaidRequestDto;
import com.example.funding.dto.response.settlement.CreatorSettlementDto;
import com.example.funding.dto.response.settlement.SettlementItem;
import com.example.funding.dto.row.SettlementSummary;
import com.example.funding.exception.*;
import com.example.funding.mapper.CreatorMapper;
import com.example.funding.mapper.ProjectMapper;
import com.example.funding.mapper.SettlementMapper;
import com.example.funding.model.Project;
import com.example.funding.model.Settlement;
import com.example.funding.service.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SettlementServiceImpl implements SettlementService {

    private final CreatorMapper creatorMapper;
    private final SettlementMapper settlementMapper;
    private final ProjectMapper projectMapper;

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

    @Override
    public ResponseEntity<ResponseDto<String>> settleProject(SettlementPaidRequestDto dto) {
        Project project = projectMapper.findById(dto.getProjectId());
        if (project == null) throw new ProjectNotFoundException();
        if (!project.getCreatorId().equals(dto.getCreatorId())) throw new AccessDeniedException();
        if (!project.getProjectStatus().equals("SUCCESS")) throw new ProjectNotSuccessException();
        if (settlementMapper.existsByProjectId(dto.getProjectId()) == 0) throw new SettlementNotFoundException();
        if (settlementMapper.isPaid(dto.getProjectId(), dto.getSettlementId()) == 1) throw new SettlementAlreadyPaidException();

        settlementMapper.updateSettlementPaid(dto);
        projectMapper.updateProjectToSettled(dto.getProjectId());
        return ResponseEntity.ok(ResponseDto.success(200, "프로젝트 정산 처리 성공", null));
    }

    @Override
    public ResponseEntity<ResponseDto<PageResult<SettlementItem>>> getSettlements(SettlementSearchCond cond, Pager pager) {
        String q = cond.getQ();
        String status = normalizeStatus(cond.getStatus());
        LocalDate from = cond.getFrom();
        LocalDate to = cond.getTo();

        int total = settlementMapper.count(q, status, from, to);
        if (total == 0) {
            return ResponseEntity.ok().body(ResponseDto.success(200, "정산 목록 조회 성공", PageResult.of(List.of(), pager, 0)));
        }

        List<SettlementItem> items = settlementMapper.findPage(
                q,
                status,
                from,
                to,
                pager.getStartRow(),
                pager.getEndRow()
        );
        return ResponseEntity.ok().body(ResponseDto.success(200, "정산 목록 조회 성공", PageResult.of(items, pager, total)));
    }

    private String normalizeStatus(String status) {
        if (status == null || status.isBlank()) return null;
        String up = status.trim().toUpperCase();
        return "ALL".equals(up) ? null : up; // ALL이면 where절에서 제외하기 위해 null
    }
}
