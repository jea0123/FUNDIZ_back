package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.creator.CreatorSettlementDto;
import com.example.funding.exception.CreatorNotFountException;
import org.springframework.http.ResponseEntity;

public interface SettlementService {
    /**
     * <p>크리에이터 ID로 정산 정보 조회</p>
     *
     * @param creatorId 크리에이터 ID
     * @return 정산 정보
     * @throws CreatorNotFountException 크리에이터를 찾을 수 없을 때
     */
    ResponseEntity<ResponseDto<CreatorSettlementDto>> getSettlementByCreatorId(Long creatorId);
}
