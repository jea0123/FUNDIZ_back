package com.example.funding.mapper;

import com.example.funding.dto.response.project.CommunityDto;
import com.example.funding.dto.response.project.ReviewDto;
import com.example.funding.model.Community;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CommunityMapper {
    List<CommunityDto> getCommunityList(@Param("projectId") Long projectId,
                                        @Param("code") String code,
                                        @Param("lastCreatedAt") LocalDateTime lastCreatedAt,
                                        @Param("lastId") Long lastId,
                                        @Param("size") int size);

    List<ReviewDto> getReviewList(@Param("projectId") Long projectId,
                                  @Param("code") String code,
                                  @Param("lastCreatedAt") LocalDateTime lastCreatedAt,
                                  @Param("lastId") Long lastId,
                                  @Param("size") int size);

    int createCommunity(Community community);

    int existsCommunityById(@Param("cmId") Long cmId);
}
