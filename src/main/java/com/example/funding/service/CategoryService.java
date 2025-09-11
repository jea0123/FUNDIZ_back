package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.model.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {
    ResponseEntity<ResponseDto<List<Category>>> getAllCategories();
}
