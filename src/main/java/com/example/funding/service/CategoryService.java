package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.model.Category;
import com.example.funding.model.Subcategory;
import org.springframework.http.ResponseEntity;

import java.util.List;

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
}
