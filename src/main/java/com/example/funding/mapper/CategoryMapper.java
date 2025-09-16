package com.example.funding.mapper;

import com.example.funding.model.Category;
import com.example.funding.model.Subcategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<Category> getAllCategories();

    List<Subcategory> getAllSubcategories();

    Subcategory findSubcategoryById(@Param("subctgrId") Long subctgrId);
}
