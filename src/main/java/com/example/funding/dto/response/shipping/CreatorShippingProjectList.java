package com.example.funding.dto.response.shipping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CreatorShippingProjectList {
    private Long creatorId;
    private Long projectId;
    private String title;
    private Long backerCnt;
    private Long completedShippingCnt;
}
