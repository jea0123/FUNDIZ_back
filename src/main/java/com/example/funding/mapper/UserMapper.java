package com.example.funding.mapper;

import com.example.funding.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User findByEmail(@Param("email") String email);

    User findByNickname(@Param("nickname") String nickname);

    void signUp(User dto);

    void updateLastLogin(@Param("userId") Long userId);

    User getUserById(@Param("userId") Long userId);
}
