package com.example.funding.mapper;

import com.example.funding.model.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<Category> getAllCategories();
}
