package com.example.funding.dto.request.settlement;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SettlementPaidRequestDto {
    @NotBlank(message = "정산 ID는 필수입니다.")
    @Positive(message = "정산 ID는 양수여야 합니다.")
    private Long settlementId;
    @NotBlank(message = "프로젝트 ID는 필수입니다.")
    @Positive(message = "프로젝트 ID는 양수여야 합니다.")
    private Long projectId;
    @NotBlank(message = "크리에이터 ID는 필수입니다.")
    @Positive(message = "크리에이터 ID는 양수여야 합니다.")
    private Long creatorId;
    @NotBlank(message = "정산 상태는 필수입니다.")
    private String settlementStatus;
}
