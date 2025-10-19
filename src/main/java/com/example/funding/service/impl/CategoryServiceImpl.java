package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.admin.analytic.CategorySuccess;
import com.example.funding.exception.badrequest.InvalidParamException;
import com.example.funding.exception.notfound.CategorySuccessNotFoundException;
import com.example.funding.mapper.CategoryMapper;
import com.example.funding.model.Category;
import com.example.funding.model.Subcategory;
import com.example.funding.service.CategoryService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static com.example.funding.validator.Preconditions.requirePositive;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<List<Category>>> getAllCategories() {
        List<Category> categories = categoryMapper.getAllCategories();
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "카테고리 조회 성공", categories));
    }

    /**
     * 모든 세부카테고리 조회
     *
     * @return 모든 세부 카테고리 리스트
     * @author 조은애
     * @since 2025-09-12
     */
    @Override
    public ResponseEntity<ResponseDto<List<Subcategory>>> getAllSubcategories() {
        List<Subcategory> subcategories = categoryMapper.getAllSubcategories();
        return ResponseEntity.ok(ResponseDto.success(200, "서브카테고리 조회 성공", subcategories));
    }

    /**
     * 카테고리별 성공률 조회
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<List<CategorySuccess>>> getCategorySuccessByCategory(Long ctgrId) {
        requirePositive(ctgrId, InvalidParamException::new);
        List<CategorySuccess> categorySuccesses = categoryMapper.getCategorySuccessByCategory(ctgrId);
        if (categorySuccesses.isEmpty()) throw new CategorySuccessNotFoundException();

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "카테고리별 성공률 조회 성공", categorySuccesses));
    }
}
