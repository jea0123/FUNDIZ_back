package com.example.funding.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Subcategory {
    private Long subctgrId;
    private Long ctgrId;
    private String subctgrName;
}
