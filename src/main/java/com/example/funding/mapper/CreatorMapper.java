package com.example.funding.mapper;

import com.example.funding.model.Creator;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CreatorMapper {
    List<Creator> findByIds(@Param("ids") List<Long> ids);
}
