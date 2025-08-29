package com.example.funding.mapper;

import com.example.funding.model.Subcategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SubcategoryMapper {
    Subcategory getSubcategoryById(@Param("projectId") Long projectId);
}
