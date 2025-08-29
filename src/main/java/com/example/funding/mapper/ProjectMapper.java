package com.example.funding.mapper;

import com.example.funding.model.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProjectMapper {
    Project getProjectById(@Param("projectId") Long projectId);

}
