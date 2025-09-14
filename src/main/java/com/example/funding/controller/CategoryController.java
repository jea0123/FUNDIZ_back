package com.example.funding.controller;

import com.example.funding.dto.ResponseDto;
import com.example.funding.model.Category;
import com.example.funding.model.Subcategory;
import com.example.funding.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 모든 카테고리 조회
     * @return 모든 카테고리 리스트
     * @author 장민규
     * @since 2025-09-11
     */
    @GetMapping("/categories")
    public ResponseEntity<ResponseDto<List<Category>>> getAllCategories() {
        return categoryService.getAllCategories();
    }

    /**
     * 모든 세부카테고리 조회
     * @return 모든 세부 카테고리 리스트
     * @author 조은애
     * @since 2025-09-12
     */
    @GetMapping("/subcategories")
    public ResponseEntity<ResponseDto<List<Subcategory>>> getAllSubcategories() {
        return categoryService.getAllSubcategories();
    }
}
