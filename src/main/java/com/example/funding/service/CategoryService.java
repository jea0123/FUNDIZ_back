package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.admin.analytic.CategorySuccess;
import com.example.funding.exception.notfound.CategorySuccessNotFoundException;
import com.example.funding.model.Category;
import com.example.funding.model.Subcategory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface CategoryService {
    /**
     * 모든 카테고리 조회
     *
     * @return 모든 카테고리 리스트
     * @author 장민규
     * @since 2025-09-11
     */
    ResponseEntity<ResponseDto<List<Category>>> getAllCategories();

    ResponseEntity<ResponseDto<List<Subcategory>>> getAllSubcategories();

    /**
     * 카테고리별 성공률 조회
     *
     * @param ctgrId 카테고리 ID
     * @return 카테고리별 성공률 데이터 리스트
     * @throws CategorySuccessNotFoundException 데이터가 존재하지 않을 경우
     * @author 장민규
     * @since 2025-09-11
     */
    ResponseEntity<ResponseDto<List<CategorySuccess>>> getCategorySuccessByCategory(@NotNull(message = "ctgrId는 필수입니다. 현재: ${validatedValue}")
                                                                                    @Positive(message = "ctgrId는 양수여야 합니다. 현재: ${validatedValue}")
                                                                                    Long ctgrId);
}
