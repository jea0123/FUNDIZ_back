package com.example.funding.mapper;

import com.example.funding.common.Pager;
import com.example.funding.dto.request.creator.ProjectCreateRequestDto;
import com.example.funding.dto.request.creator.SearchCreatorProjectDto;
import com.example.funding.dto.response.creator.CreatorProjectDetailDto;
import com.example.funding.dto.response.creator.CreatorProjectListDto;
import com.example.funding.model.Creator;
import com.example.funding.model.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CreatorMapper {
    List<Creator> findByIds(@Param("ids") List<Long> ids);

    Creator findById(@Param("creatorId") Long creatorId);

//    CreatorPDetailDto getCreatorPDetailDto(@Param("creatorId") Long creatorId);

    int countProject(@Param("creatorId") Long creatorId, @Param("dto") SearchCreatorProjectDto dto);

    List<CreatorProjectListDto> getProjectList(@Param("creatorId") Long creatorId, @Param("dto") SearchCreatorProjectDto dto, @Param("pager") Pager pager);

    int saveProject(ProjectCreateRequestDto dto);

    int updateProject(@Param("creatorId") Long creatorId, @Param("project") Project project);

    int deleteProject(@Param("projectId") Long projectId);

    int markVerifyProject(@Param("projectId") Long projectId);

    CreatorProjectDetailDto getProjectDetail(@Param("projectId") Long projectId, @Param("creatorId") Long creatorId);
}
