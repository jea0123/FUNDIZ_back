package com.example.funding.dao;

import com.example.funding.model.User;

public interface UserDao {
    User findByEmail(String email);

    User findByNickname(String nickname);

    void signUp(User dto);

    void updateLastLogin(Long userId);

    User getUserById(Long userId);
}
