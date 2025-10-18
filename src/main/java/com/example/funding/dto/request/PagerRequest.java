package com.example.funding.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagerRequest {
    @Min(value = 1, message = "페이지 번호는 1 이상이어야 합니다.")
    private Integer page;
    @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
    private Integer size;
    @Min(value = 1, message = "그룹당 페이지 수는 1 이상이어야 합니다.")
    private Integer perGroup;
}
