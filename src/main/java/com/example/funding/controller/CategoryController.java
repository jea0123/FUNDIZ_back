package com.example.funding.controller;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.category.CreateCategoryDto;
import com.example.funding.dto.request.category.CreateSubCategoryDto;
import com.example.funding.model.Category;
import com.example.funding.model.Subcategory;
import com.example.funding.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 모든 카테고리 조회
     *
     * @return 모든 카테고리 리스트
     * @author 장민규
     * @since 2025-09-11
     */
    @GetMapping
    public ResponseEntity<ResponseDto<List<Category>>> getAllCategories() {
        return categoryService.getAllCategories();
    }

    /**
     * 모든 세부카테고리 조회
     *
     * @return 모든 세부 카테고리 리스트
     * @author 조은애
     * @since 2025-09-12
     */
    @GetMapping("/subcategories")
    public ResponseEntity<ResponseDto<List<Subcategory>>> getAllSubcategories() {
        return categoryService.getAllSubcategories();
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDto<String>> createCategory(@RequestBody CreateCategoryDto dto) {
        return categoryService.createCategory(dto);
    }

    @PostMapping("/subcategories/create")
    public ResponseEntity<ResponseDto<String>> createSubCategory(@RequestBody CreateSubCategoryDto dto) {
        return categoryService.createSubCategory(dto);
    }
}
