package com.example.funding.mapper;

import com.example.funding.dto.response.project.SubcategoryDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SubcategoryMapper {
    SubcategoryDto getSubcategoryById(@Param("subctgrId") Long subctgrId);
}
