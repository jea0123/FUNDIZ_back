package com.example.funding.dto.response.admin.analytic;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingByCategory {
    private String categoryName;
    private Double avgRating;
}
