package com.example.funding.mapper;

import com.example.funding.common.Pager;
import com.example.funding.dto.request.project.SearchProjectDto;
import com.example.funding.dto.response.backing.BackingCreatorProjectListDto;
import com.example.funding.dto.response.project.FeaturedProjectDto;
import com.example.funding.dto.response.project.RecentTop10ProjectDto;
import com.example.funding.dto.response.shipping.CreatorShippingProjectList;
import com.example.funding.dto.row.ProjectRow;
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

    int getProjectCnt(@Param("creatorId") Long creatorId);

    String getStatus(@Param("projectId") Long projectId);

    List<FeaturedProjectDto> searchProjects(@Param("dto") SearchProjectDto dto, @Param("pager") Pager pager);

    int countSearchProjects(@Param("dto") SearchProjectDto dto);

    int updateProjectsToOpen();

    int updateProjectsToSuccess();

    int updateProjectsToFailed();

    void updateProjectSettled(@Param("projectId") Long projectId, @Param("status") String status);

    LocalDate getProjectEndDate(@Param("projectId") Long projectId);

    long getVerifyingCnt(Long creatorId);

    List<BackingCreatorProjectListDto> getBackingCreatorProjectList(Long creatorId);

    List<CreatorShippingProjectList> getCShippingList(Long creatorId);

    void increaseLikeCnt(@Param("projectId") Long projectId);

    void decreaseLikeCnt(@Param("projectId") Long projectId);
}
