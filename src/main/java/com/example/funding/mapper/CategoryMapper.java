package com.example.funding.mapper;

import com.example.funding.model.Category;
import com.example.funding.model.Subcategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<Category> getAllCategories();

    List<Subcategory> getAllSubcategories();

    Subcategory findSubcategoryById(Long subctgrId);
}
