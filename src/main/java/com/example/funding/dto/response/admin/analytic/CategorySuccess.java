package com.example.funding.dto.response.admin.analytic;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategorySuccess {
    private String categoryName;
    private Long successCnt;
    private Long failCnt;
}
