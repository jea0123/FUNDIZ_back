package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.admin.SettlementPaidRequestDto;
import com.example.funding.dto.response.creator.CreatorSettlementDto;
import com.example.funding.exception.AccessDeniedException;
import com.example.funding.exception.CreatorNotFountException;
import com.example.funding.exception.ProjectNotFoundException;
import com.example.funding.exception.ProjectNotSuccessException;
import org.springframework.http.ResponseEntity;

public interface SettlementService {
    /**
     * <p>크리에이터 ID로 정산 정보 조회</p>
     *
     * @param creatorId 크리에이터 ID
     * @return 정산 정보
     * @throws CreatorNotFountException 크리에이터를 찾을 수 없을 때
     * @author 장민규
     * @since 2025-10-13
     */
    ResponseEntity<ResponseDto<CreatorSettlementDto>> getSettlementByCreatorId(Long creatorId);

    /**
     * <p>프로젝트 정산 처리</p>
     *
     * @param dto 정산 요청 DTO
     * @return 정산 처리 결과
     * @throws ProjectNotFoundException 존재하지 않는 프로젝트일 때
     * @throws AccessDeniedException 접근 권한이 없을 때
     * @throws ProjectNotSuccessException 프로젝트가 성공 상태가 아닐 때
     * @author 장민규
     * @since 2025-10-13
     */
    ResponseEntity<ResponseDto<String>> settleProject(SettlementPaidRequestDto dto);
}
