package com.lpy.news.dao;

import com.lpy.news.dto.UserDto;
import com.lpy.news.entity.User;

import java.util.List;

public interface UserDao {
    User userLogin(User user);

    User getUserEmail(String email);

    void userRegister(User user);

    void setNewPasswd(String email, String newPasswd);

    User selectUserById(Long id);

    List<UserDto> queryUserPage(int pageNo, int pageSize, String name, String email);

    int queryUserCount(int pageNo, int pageSize, String name, String email);

    void removeUser(Long id);

    void updateUser(User user);

    //以下为算法使用接口
    List<Long> getAllUserIdList();
}
