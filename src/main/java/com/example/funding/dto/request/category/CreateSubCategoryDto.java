package com.example.funding.dto.request.category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSubCategoryDto {
    private Long ctgrId;
    private String subctgrName;
}
