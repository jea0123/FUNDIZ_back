package com.example.funding.dao;

import com.example.funding.model.User;

public interface UserDao {
    User findByEmail(String email);

    void signUp(User dto);
}
