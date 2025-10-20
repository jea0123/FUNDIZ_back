package com.example.funding.mapper;

import com.example.funding.common.Pager;
import com.example.funding.dto.response.creator.CreatorFollowerDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FollowMapper {
    int isFollowingCreator(@Param("userId") Long userId, @Param("creatorId") Long creatorId);

    void followCreator(@Param("userId") Long userId, @Param("creatorId") Long creatorId);

    void unfollowCreator(@Param("userId") Long userId, @Param("creatorId") Long creatorId);

    Long getFollowerCnt(@Param("creatorId") Long creatorId);

    List<CreatorFollowerDto> getCreatorFollowers(@Param("creatorId") Long creatorId, @Param("loginUserId") Long loginUserId, @Param("pager") Pager pager);

    int countCreatorFollowers(@Param("creatorId") Long creatorId);
}
