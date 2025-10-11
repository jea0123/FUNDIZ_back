package com.example.funding.mapper;

import com.example.funding.dto.response.user.MyPageLikedDto;
import com.example.funding.dto.response.user.MyPageQnADto;
import com.example.funding.dto.response.user.RecentViewProject;
import com.example.funding.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    User findByEmail(@Param("email") String email);

    User findByNickname(@Param("nickname") String nickname);

    void signUp(User dto);

    void updateLastLogin(@Param("userId") Long userId);

    User getUserById(@Param("userId") Long userId);

    List<MyPageLikedDto> getLikedList(@Param("userId") Long userId);

    List<MyPageQnADto> getQnAList(@Param("userId") Long userId);

    List<RecentViewProject> getRecentViewProjects(@Param("userId") Long userId);

    int updateNickname(@Param("userId") Long userId, @Param("nickname") String nickname);

    int updatePwd(@Param("userId") Long userId, @Param("password") String pwd);

    int updateProfile(@Param("userId") Long userId, @Param("profileImg") String profile);

    Long getCreatorIdByUserId(Long userId);

    Boolean suspendedCreator(@Param("creatorId") Long creatorId);
}
