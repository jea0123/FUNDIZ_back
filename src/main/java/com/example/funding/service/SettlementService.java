package com.example.funding.service;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.settlement.SettlementPaidRequestDto;
import com.example.funding.dto.request.settlement.SettlementSearchCond;
import com.example.funding.dto.response.settlement.CreatorSettlementDto;
import com.example.funding.dto.response.settlement.SettlementItem;
import com.example.funding.dto.row.SettlementSummary;
import com.example.funding.exception.badrequest.ProjectNotSuccessException;
import com.example.funding.exception.badrequest.SettlementStatusAlreadyChangedException;
import com.example.funding.exception.forbidden.AccessDeniedException;
import com.example.funding.exception.notfound.CreatorNotFoundException;
import com.example.funding.exception.notfound.ProjectNotFoundException;
import com.example.funding.exception.notfound.SettlementNotFoundException;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@Validated
public interface SettlementService {
    /**
     * <p>크리에이터 ID로 정산 정보 조회</p>
     *
     * @param creatorId 크리에이터 ID
     * @return 정산 정보
     * @throws CreatorNotFoundException 크리에이터를 찾을 수 없을 때
     * @author 장민규
     * @since 2025-10-13
     */
    ResponseEntity<ResponseDto<CreatorSettlementDto>> getSettlementByCreatorId(
            @NotNull(message = "creatorId는 필수입니다. 현재: ${validatedValue}")
            @Positive(message = "creatorId는 양수여야 합니다. 현재: ${validatedValue}")
            Long creatorId);

    /**
     * <p>정산 상태 변경</p>
     *
     * @param dto 정산 상태 요청 DTO
     * @return 정산 상태 처리 결과
     * @throws ProjectNotFoundException                존재하지 않는 프로젝트일 때
     * @throws AccessDeniedException                   접근 권한이 없을 때
     * @throws ProjectNotSuccessException              프로젝트가 성공 상태가 아닐 때
     * @throws SettlementNotFoundException             정산 정보를 찾을 수 없을 때
     * @throws SettlementStatusAlreadyChangedException 이미 변경된 상태일 때
     * @author 장민규
     * @since 2025-10-13
     */
    ResponseEntity<ResponseDto<String>> updateStatus(SettlementPaidRequestDto dto);

    /**
     * 정산 목록 조회 (검색 + 페이징)
     *
     * @param cond  검색조건(q/status/from/to)
     * @param pager 1-based Pager (getStartRow/getEndRow 사용)
     * @return 페이지 결과
     * @author 장민규
     * @since 2025-10-13
     */
    ResponseEntity<ResponseDto<PageResult<SettlementItem>>> getSettlements(SettlementSearchCond cond, Pager pager);

    /**
     * <p>전체 정산 요약 정보 조회</p>
     *
     * @return 전체 정산 요약 정보
     * @author 장민규
     * @since 2025-10-13
     */
    ResponseEntity<ResponseDto<SettlementSummary>> getSettlementSummary();
}
