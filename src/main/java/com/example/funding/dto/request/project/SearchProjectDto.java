package com.example.funding.dto.request.project;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchProjectDto {
    private String keyword;
    private Long ctgrId;
    private Long subctgrId;
    private String sort = "view";
    //종료 전만 보기
    private Boolean activeOnly = true;
}
