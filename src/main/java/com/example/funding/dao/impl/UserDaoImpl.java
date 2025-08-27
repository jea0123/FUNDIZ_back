package com.example.funding.dao.impl;

import com.example.funding.dao.UserDao;
import com.example.funding.model.User;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private final SqlSession session;

    @Override
    public User findByEmail(String email) {
        return session.selectOne("user.findByEmail", email);
    }

    @Override
    public User findByNickname(String nickname) {
        return session.selectOne("user.findByNickname", nickname);
    }

    @Override
    public void signUp(User user) {
        session.insert("user.signUp", user);
    }

    @Override
    public void updateLastLogin(Long userId) {
        session.update("user.updateLastLogin", userId);
    }
}
