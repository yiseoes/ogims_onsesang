// src/com/semi/service/user/UserService.java
package com.semi.service.user;

import com.semi.domain.User;

public interface UserService {

    // 회원가입
    void addUser(User user) throws Exception;

    // 단건 조회(내정보/상세)
    User getUser(String userId) throws Exception;

    // 로그인(상태 Y만 허용)
    User login(String userId, String plainPassword) throws Exception;

    // 정보 수정(비번 제외)
    void updateUser(User user) throws Exception;

    // 비밀번호 변경
    void updatePassword(String userId, String oldPlain, String newPlain) throws Exception;

    // 회원탈퇴 → status='N'
    void deactivateUser(String userId) throws Exception;

    // 중복체크(선택)
    boolean exists(String userId) throws Exception;
}
