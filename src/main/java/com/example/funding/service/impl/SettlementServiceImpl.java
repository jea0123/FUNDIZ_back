package com.example.funding.service.impl;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.settlement.SettlementPaidRequestDto;
import com.example.funding.dto.request.settlement.SettlementSearchCond;
import com.example.funding.dto.response.settlement.CreatorSettlementDto;
import com.example.funding.dto.response.settlement.SettlementItem;
import com.example.funding.dto.row.SettlementSummary;
import com.example.funding.enums.NotificationType;
import com.example.funding.exception.badrequest.ProjectNotSuccessException;
import com.example.funding.exception.badrequest.SettlementStatusAlreadyChangedException;
import com.example.funding.handler.NotificationPublisher;
import com.example.funding.mapper.ProjectMapper;
import com.example.funding.mapper.SettlementMapper;
import com.example.funding.model.Creator;
import com.example.funding.model.Project;
import com.example.funding.model.Settlement;
import com.example.funding.service.SettlementService;
import com.example.funding.validator.Loaders;
import com.example.funding.validator.PermissionChecker;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.funding.validator.Preconditions.requireIn;

@Service
@RequiredArgsConstructor
@Transactional
@Validated
public class SettlementServiceImpl implements SettlementService {
    private final Loaders loaders;
    private final PermissionChecker auth;
    private final SettlementMapper settlementMapper;
    private final ProjectMapper projectMapper;

    private final NotificationPublisher notificationPublisher;

    /**
     * 정산정보 조회
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<CreatorSettlementDto>> getSettlementByCreatorId(@NotBlank Long creatorId) {
        loaders.creator(creatorId);

        List<Settlement> settlement = settlementMapper.getByCreatorId(creatorId);
        SettlementSummary settlementSummary = settlementMapper.getSettlementSummaryByCreatorId(creatorId);
        CreatorSettlementDto dtos = CreatorSettlementDto.builder()
                .settlement(settlement)
                .settlementSummary(settlementSummary)
                .build();
        return ResponseEntity.ok(ResponseDto.success(200, "크리에이터 정산 정보 조회 성공", dtos));
    }

    /**
     * 정산 상태 변경
     */
    @Override
    public ResponseEntity<ResponseDto<String>> updateStatus(SettlementPaidRequestDto dto) {
        Project project = loaders.project(dto.getProjectId());
        Creator existingCreator = loaders.creator(dto.getCreatorId());

        auth.mustBeOwner(dto.getCreatorId(), existingCreator.getCreatorId());
        requireIn(dto.getSettlementStatus(), List.of("PAID", "SETTLED"), () -> new IllegalArgumentException("유효하지 않은 정산 상태입니다."));
        requireIn(project.getProjectStatus(), List.of("SUCCESS", "SETTLED"), ProjectNotSuccessException::new);

        loaders.settlement(dto.getProjectId());
        if (settlementMapper.getStatus(dto.getProjectId(), dto.getCreatorId(), dto.getSettlementId()).equals(dto.getSettlementStatus()))
            throw new SettlementStatusAlreadyChangedException();

        settlementMapper.updateSettlementStatus(dto);
        String projectStatus = dto.getSettlementStatus().equals("PAID") ? "SETTLED" : projectMapper.getStatus(dto.getProjectId());
        projectMapper.updateProjectSettled(dto.getProjectId(), projectStatus);
        if (projectStatus.equals("SETTLED")) {
            notificationPublisher.publish(existingCreator.getUserId(), NotificationType.FUNDING_SETTLED, project.getTitle(), dto.getSettlementId());
        } else {
            notificationPublisher.publish(existingCreator.getUserId(), NotificationType.FUNDING_SETTLED_CANCELLED, project.getTitle(), dto.getSettlementId());
        }
        return ResponseEntity.ok(ResponseDto.success(200, "정산 상태 변경 성공", null));
    }

    /**
     * 정산 목록 조회
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<PageResult<SettlementItem>>> getSettlements(SettlementSearchCond cond, Pager pager) {
        String q = cond.q();
        String status = normalizeStatus(cond.status());
        LocalDateTime from = cond.from();
        LocalDateTime to = cond.to();

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

    /**
     * 정산 요약 정보 조회
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<SettlementSummary>> getSettlementSummary() {
        SettlementSummary summary = settlementMapper.getSettlementSummary();
        return ResponseEntity.ok(ResponseDto.success(200, "정산 요약 정보 조회 성공", summary));
    }

    /**
     * 상태값 정규화
     *
     * @param status 상태값
     * @return 정규화된 상태값 (ALL인 경우 null 반환)
     */
    private String normalizeStatus(String status) {
        if (status == null || status.isBlank()) return null;
        String up = status.trim().toUpperCase();
        return "ALL".equals(up) ? null : up;
    }
}
