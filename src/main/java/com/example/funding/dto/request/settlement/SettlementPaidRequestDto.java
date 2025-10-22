package com.example.funding.dto.request.settlement;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SettlementPaidRequestDto {
    @NotNull(message = "정산 ID는 필수입니다.")
    @Positive(message = "정산 ID는 양수여야 합니다.")
    private Long settlementId;
    @NotNull(message = "프로젝트 ID는 필수입니다.")
    @Positive(message = "프로젝트 ID는 양수여야 합니다.")
    private Long projectId;
    @NotNull(message = "크리에이터 ID는 필수입니다.")
    @Positive(message = "크리에이터 ID는 양수여야 합니다.")
    private Long creatorId;
    @NotBlank(message = "정산 상태는 필수입니다.")
    private String settlementStatus;
}
