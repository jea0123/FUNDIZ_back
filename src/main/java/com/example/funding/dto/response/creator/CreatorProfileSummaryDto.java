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
}
