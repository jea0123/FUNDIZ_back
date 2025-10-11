package com.example.funding.dto.response.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SubcategoryWithParentDto {
    private Long subctgrId;
    private Long parentCtgrId;
}
