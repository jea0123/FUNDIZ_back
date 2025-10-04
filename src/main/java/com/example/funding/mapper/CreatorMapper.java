package com.example.funding.mapper;

import com.example.funding.dto.request.project.ProjectCreateRequestDto;
import com.example.funding.dto.response.creator.CreatorPDetailDto;
import com.example.funding.model.Creator;
import com.example.funding.model.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CreatorMapper {
    List<Creator> findByIds(@Param("ids") List<Long> ids);

    Creator findById(@Param("creatorId") Long creatorId);

    CreatorPDetailDto getCreatorPDetailDto(@Param("creatorId") Long creatorId);

    int saveProject(ProjectCreateRequestDto dto);

    int updateProject(Project project);

    int deleteProject(@Param("projectId") Long projectId);

    int markVerifyProject(@Param("projectId") Long projectId);
}
