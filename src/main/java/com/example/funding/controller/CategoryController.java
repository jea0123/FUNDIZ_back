package com.example.funding.controller;

import com.example.funding.dto.ResponseDto;
import com.example.funding.model.Category;
import com.example.funding.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<ResponseDto<List<Category>>> getAllCategories() {
        return categoryService.getAllCategories();
    }
}
