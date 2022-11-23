package com.lpy.news.service;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.dto.UserDto;
import com.lpy.news.entity.User;

public interface UserService {
    User userLogin(User user);

    User getUserEmail(String email);

    void userRegister(User user);

    void sendMsg(String to, String subject, String text);

    void setNewPasswd(String email, String newPasswd);

    User selectUserById(Long id);

    BasePageResponse<UserDto> queryUserPage(int pageNo, int pageSize, String name, String email);

    void removeUser(Long[] ids);

    void updateUser(User user);
}
