package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.project.SubcategoryDto;
import com.example.funding.mapper.CategoryMapper;
import com.example.funding.model.Category;
import com.example.funding.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    /**
     * 모든 카테고리 조회
     * @return 모든 카테고리 리스트
     * @author 장민규
     * @since 2025-09-11
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<List<Category>>> getAllCategories() {
        List<Category> categories = categoryMapper.getAllCategories();
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200,"카테고리 조회 성공", categories));
    }

    @Override
    public ResponseEntity<ResponseDto<List<SubcategoryDto>>> getAllSubcategories() {
        List<SubcategoryDto> subcategories = categoryMapper.getAllSubcategories();
        return ResponseEntity.ok(ResponseDto.success(200, "서브카테고리 조회 성공", subcategories));
    }
}
