package com.example.funding.mapper;

import com.example.funding.dto.response.creator.ReviewListDto;
import com.example.funding.dto.response.user.MyPageLikedDto;
import com.example.funding.dto.response.user.RecentViewProject;
import com.example.funding.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    User findByEmail(@Param("email") String email);

    User findByNickname(@Param("nickname") String nickname);

    void signUp(User dto);

    void updateLastLogin(@Param("userId") Long userId);

    User getUserById(@Param("userId") Long userId);

    List<MyPageLikedDto> getLikedList(@Param("userId") Long userId);

    void upsertRecentView(@Param("userId") Long userId, @Param("projectId") Long projectId);

    List<RecentViewProject> getRecentViewProjects(@Param("userId") Long userId);

    void updateNickname(@Param("userId") Long userId, @Param("nickname") String nickname);

    void updatePwd(@Param("userId") Long userId, @Param("password") String pwd);

    void updateProfile(@Param("userId") Long userId, @Param("profileImg") String profile);

    Long getCreatorIdByUserId(Long userId);

    void withdrawUser(@Param("userId") Long userId);

    void likeProject(@Param("userId") Long userId, @Param("projectId") Long projectId);

    int isProjectLiked(@Param("userId") Long userId, @Param("projectId") Long projectId);

    void dislikeProject(@Param("userId") Long userId, @Param("projectId") Long projectId);

    LocalDateTime getLastLoginTime(@Param("userId") Long userId);

    List<Map<String, Object>> selectUsersByIds(@Param("list") List<Long> userIds);
}
