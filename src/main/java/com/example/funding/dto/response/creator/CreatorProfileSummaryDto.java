package com.example.funding.dto.response.creator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreatorProfileSummaryDto {
    private Long creatorId;
    private String creatorName;
    private String businessNum;
    private String email;
    private String phone;

    // 필수 플래그
    private Boolean isComplete; // 창작자명/사업자번호/이메일/전화번호 모두 충족
    private Boolean isSuspended; // 정지 여부
}
