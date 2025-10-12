package com.example.funding.dto.request.project;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewCreateRequestDto {
    private String cmContent;
    private Integer rating;
}
