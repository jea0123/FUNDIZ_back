package com.example.funding.mapper;

import com.example.funding.model.News;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NewsMapper {
    List<News> getNewsListById(@Param("projectId") Long projectId);
}
