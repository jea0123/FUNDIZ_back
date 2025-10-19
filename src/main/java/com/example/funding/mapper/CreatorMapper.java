package com.example.funding.mapper;

import com.example.funding.common.Pager;
import com.example.funding.dto.request.creator.CreatorUpdateRequestDto;
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

    Creator findById(@Param("creatorId") Long creatorId);

    int countProject(@Param("creatorId") Long creatorId, @Param("dto") SearchCreatorProjectDto dto);

    List<CreatorProjectListDto> getProjectList(@Param("creatorId") Long creatorId, @Param("dto") SearchCreatorProjectDto dto, @Param("pager") Pager pager);

    int saveProject(Project project);

    void updateProject(Project project);

    void deleteProject(@Param("projectId") Long projectId);

    void markVerifyProject(@Param("projectId") Long projectId);

    CreatorProjectDetailDto getProjectDetail(@Param("projectId") Long projectId, @Param("creatorId") Long creatorId);

    int existsCreator(@Param("creatorId") Long creatorId);

    CreatorProfileSummaryDto getCreatorProfileSummary(@Param("creatorId") Long creatorId);

    int qnaTotalOfCreator(Long creatorId);

    List<CreatorQnaDto> getQnaListOfCreator(Long creatorId, @Param("pager") Pager pager);

    List<CreatorDashboardRankDto> getProjectRankDate(Long creatorId);

    CreatorDashboardDto creatorDashboardDto(Long creatorId);

    int updateCreatorInfo(CreatorUpdateRequestDto dto);

    Creator creatorInfo(Long creatorId);

    void increaseFollowersCount(@Param("creatorId") Long creatorId);

    void decreaseFollowersCount(@Param("creatorId") Long creatorId);

    CreatorSummaryDto.CreatorRow getCreatorRowById(@Param("creatorId") Long creatorId);

    CreatorSummaryDto.Stats getCreatorStatsById(@Param("creatorId") Long creatorId);
}
