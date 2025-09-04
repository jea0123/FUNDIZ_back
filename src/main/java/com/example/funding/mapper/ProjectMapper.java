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

    List<Project> findByIdsAndStatus(@Param("ids") List<Long> ids,@Param("status") String status);

    // 최근 N일 이내 시작 & FUNDING 상태에서 알고리즘 점수로 상위 limit
    List<Project> findFeatured(
            @Param("days") int days,
            @Param("limit") int limit
    );

    List<Project> findFeaturedExcluding(
            @Param("limit") int limit,
            @Param("excludeIds") List<Long> excludeIds
    );
}
