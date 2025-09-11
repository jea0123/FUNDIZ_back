package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.mapper.CategoryMapper;
import com.example.funding.model.Category;
import com.example.funding.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public ResponseEntity<ResponseDto<List<Category>>> getAllCategories() {
        List<Category> categories = categoryMapper.getAllCategories();
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200,"카테고리 조회 성공", categories));
    }
}
