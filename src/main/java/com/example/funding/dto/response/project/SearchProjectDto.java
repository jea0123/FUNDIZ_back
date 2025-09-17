package com.example.funding.dto.response.project;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchProjectDto {
    //검색어
    private String keyword;
    //카테고리 필터
    private Long ctgrId;
    //서브카테고리
    private Long subctgrId;
    private String sort = "score";
    //종료 전만 보기
    private Boolean activeOnly = true;
}
