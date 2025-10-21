package com.example.funding.controller;

import com.example.funding.common.CustomUserPrincipal;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.settlement.CreatorSettlementDto;
import com.example.funding.exception.notfound.CreatorNotFoundException;
import com.example.funding.service.SettlementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/settlement")
@RequiredArgsConstructor
public class SettlementController {
    private final SettlementService settlementService;

    /**
     * <p>크리에이터 ID로 정산 정보 조회</p>
     *
     * @param principal 인증된 사용자 정보
     * @return 정산 정보
     * @throws CreatorNotFoundException 크리에이터를 찾을 수 없을 때 (404)
     * @author 장민규
     * @since 2025-10-13
     */
    @GetMapping("/creator")
    public ResponseEntity<ResponseDto<CreatorSettlementDto>> getSettlementByCreatorId(
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        return settlementService.getSettlementByCreatorId(principal.creatorId());
    }
}
