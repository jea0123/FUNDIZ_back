package com.example.funding.mapper;

import com.example.funding.common.Pager;
import com.example.funding.dto.request.project.SearchProjectDto;
import com.example.funding.dto.row.ProjectRow;
import com.example.funding.dto.request.project.ProjectCreateRequestDto;
import com.example.funding.dto.response.project.FeaturedProjectDto;
import com.example.funding.dto.response.project.RecentTop10ProjectDto;
import com.example.funding.model.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ProjectMapper {
    Project findById(@Param("projectId") Long projectId);

    ProjectRow getProjectRow(@Param("projectId") Long projectId);

    void updateViewCnt(@Param("projectId") Long projectId);

    List<RecentTop10ProjectDto> findRecentTopProjectsJoined(@Param("since") LocalDate since,
                                                            @Param("startDays") int startDays,
                                                            @Param("limit") int limit);

    List<FeaturedProjectDto> findFeaturedJoinedWithRecent(@Param("days") int days,
                                                          @Param("limit") int limit);

    List<Project> findFeaturedExcluding(
            @Param("limit") int limit,
            @Param("excludeIds") List<Long> excludeIds
    );

    int getProjectCnt(@Param("creatorId") Long creatorId);

    int saveProject(ProjectCreateRequestDto dto);

    String getStatus(@Param("projectId") Long projectId);

    int updateProject(Project project);

    List<FeaturedProjectDto> searchProjects(@Param("dto") SearchProjectDto dto, @Param("pager") Pager pager);

    int countSearchProjects(@Param("dto") SearchProjectDto dto);

    int deleteProject(@Param("projectId") Long projectId);

    int updateProjectsToOpen();

    int updateProjectsToSuccess();

    int updateProjectsToFailed();

    int markRequestReview(@Param("projectId") Long projectId);

    String getReason(@Param("projectId") Long projectId);
}
