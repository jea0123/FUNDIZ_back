package com.example.funding.dto.response.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SubcategoryDto {
    private Long ctgrId;
    private String ctgrName;
    private Long subctgrId;
    private String subctgrName;
}
