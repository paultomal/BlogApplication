package com.example.nafiz.blog.user;

import com.example.nafiz.blog.common.ChangePassword;

import java.util.Map;

public interface UserService {
    Map<String, Object> saveUser(UserInfoDTO userDTO);
    Map<String, Object> getAllUsers();
    Map<String, Object> getUserById(Long id);
    Map<String, Object> updateUser(Long id, UserInfoDTO userDTO);
    Map<String, Object> deleteUser(Long id);
    Map<String, Object> changePassword(Long id, ChangePassword changePassword);
}
