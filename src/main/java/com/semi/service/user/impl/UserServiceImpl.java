// src/com/semi/service/user/impl/UserServiceImpl.java
package com.semi.service.user.impl;

import com.semi.common.util.PasswordUtilSHA256;
import com.semi.domain.User;
import com.semi.service.user.UserService;
import com.semi.service.user.dao.UserDao;

public class UserServiceImpl implements UserService {

    private final UserDao userDao = new UserDao();

    @Override
    public void addUser(User user) throws Exception {
        System.out.println("[USER][SRV] addUser " + user.getUserId());
        if (userDao.exists(user.getUserId()))
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");

        // 필수값 기본 세팅
        user.setStatus("Y");

        // 비밀번호 해시
        user.setPassword(PasswordUtilSHA256.hash(user.getPassword()));
        userDao.insert(user);
    }

    @Override
    public User getUser(String userId) throws Exception {
        return userDao.findById(userId);
    }

    @Override
    public User login(String userId, String plainPassword) throws Exception {
        System.out.println("[USER][SRV] login " + userId);
        User u = userDao.findById(userId);
        if (u == null) throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
        if (!"Y".equalsIgnoreCase(u.getStatus()))
            throw new IllegalStateException("아이디가 존재하지 않습니다.");

        if (!PasswordUtilSHA256.matches(plainPassword, u.getPassword()))
            throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");

        // 비밀번호는 세션 보안상 제거해서 넘겨도 됨
        u.setPassword(null);
        return u;
    }

    @Override
    public void updateUser(User user) throws Exception {
        System.out.println("[USER][SRV] updateUser " + user.getUserId());
        // 비번 제외 업데이트
        userDao.update(user);
    }

    @Override
    public void updatePassword(String userId, String oldPlain, String newPlain) throws Exception {
        System.out.println("[USER][SRV] updatePassword " + userId);
        User u = userDao.findById(userId);
        if (u == null) throw new IllegalArgumentException("존재하지 않는 사용자입니다.");

        userDao.updatePassword(userId, PasswordUtilSHA256.hash(newPlain));
    }

    @Override
    public void deactivateUser(String userId) throws Exception {
        userDao.setStatus(userId, "N");
    }

    @Override
    public boolean exists(String userId) throws Exception {
        return userDao.exists(userId);
    }
}
