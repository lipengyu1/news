package com.lpy.news.service.impl;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.dao.UserDao;
import com.lpy.news.dto.UserDto;
import com.lpy.news.entity.User;
import com.lpy.news.service.SnowService;
import com.lpy.news.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    @Value("${spring.mail.username}")
    private String from;
    //发送邮件需要的对象
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    UserDao userDao;
    SnowService snowService = new SnowService(1, 1);

    @Override
    public User userLogin(User user) {
        return userDao.userLogin(user);
    }

    @Override
    public User getUserEmail(String email) {
        return userDao.getUserEmail(email);
    }

    @Override
    public void userRegister(User user) {
        String password = user.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        user.setPassword(password);
        user.setId(snowService.getId());
        userDao.userRegister(user);
    }

    @Override
    public void sendMsg(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    @Override
    public void setNewPasswd(String email, String newPasswd) {
        userDao.setNewPasswd(email,newPasswd);
    }

    @Override
    public User selectUserById(Long id) {
        return userDao.selectUserById(id);
    }

    @Override
    public BasePageResponse<UserDto> queryUserPage(int pageNo, int pageSize, String name, String email) {
        int pageNo1 = pageSize * (pageNo - 1);
        List<UserDto> queryList = userDao.queryUserPage(pageNo1, pageSize, name,email);
        ArrayList<UserDto> arrayList = new ArrayList<>(queryList);
        int totalCount = userDao.queryUserCount(pageNo1, pageSize, name,email);
        BasePageResponse<UserDto> basePageResponse = new BasePageResponse<>();
        basePageResponse.setPageNo(pageNo);
        basePageResponse.setPageSize(pageSize);
        basePageResponse.setTotalPage((int) Math.ceil((float) totalCount / pageSize));
        basePageResponse.setResultList(arrayList);
        basePageResponse.setTotalCount(totalCount);
        return basePageResponse;
    }

    @Override
    public void removeUser(Long[] ids) {
        for (int i = 0; i < ids.length; i++) {
            Long id = ids[i];
            userDao.removeUser(id);
        }
    }

    @Override
    public void updateUser(User user) {
        userDao.updateUser(user);
    }
}
