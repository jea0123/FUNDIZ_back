package com.example.funding.mapper;

import com.example.funding.common.Pager;
import com.example.funding.dto.request.creator.ProjectCreateRequestDto;
import com.example.funding.dto.request.creator.SearchCreatorProjectDto;
import com.example.funding.dto.response.creator.*;
import com.example.funding.model.Creator;
import com.example.funding.model.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CreatorMapper {
    void insertCreator(Creator creator);

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

    int existsCreator(@Param("creatorId") Long creatorId);

    int hasRequiredCreatorProfile(@Param("creatorId") Long creatorId);

    CreatorProfileSummaryDto getCreatorProfileSummary(@Param("creatorId") Long creatorId);

    int qnaTotalOfCreator(Long creatorId);

    List<CreatorQnaDto> getQnaListOfCreator(Long creatorId, @Param("pager") Pager pager);

    List<CreatorDashboardRankDto> getProjectRankDate(Long creatorId);

    CreatorDashboardDto creatorDashboardDto(Long creatorId);
}
