package com.example.funding.mapper;

import com.example.funding.model.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProjectMapper {
    Project getProjectById(@Param("projectId") Long projectId);

    void updateViewCnt(@Param("projectId") Long projectId);

    List<Project> findByIds(@Param("ids") List<Long> ids);
}
