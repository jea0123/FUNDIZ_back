package com.example.funding.mapper;

import com.example.funding.dto.request.project.ProjectCreateRequestDto;
import com.example.funding.dto.response.project.FeaturedProjectDto;
import com.example.funding.dto.response.project.RecentTop10ProjectDto;
import com.example.funding.model.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface ProjectMapper {
    Project getProject(@Param("projectId") Long projectId);

    void updateViewCnt(@Param("projectId") Long projectId);

    List<RecentTop10ProjectDto> findRecentTopProjectsJoined(@Param("since") Date since,
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

    String getStatus(Long projectId);
}
