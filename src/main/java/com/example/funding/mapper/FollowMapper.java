package com.example.funding.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FollowMapper {
    int isFollowingCreator(@Param("userId") Long userId, @Param("creatorId") Long creatorId);

    void followCreator(@Param("userId") Long userId, @Param("creatorId") Long creatorId);

    void unfollowCreator(@Param("userId") Long userId, @Param("creatorId") Long creatorId);

    Long getFollowerCnt(@Param("creatorId") Long creatorId);
}
