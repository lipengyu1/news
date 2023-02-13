package com.lpy.news.service.impl;

import com.lpy.news.dao.UserMessageDao;
import com.lpy.news.dto.UserMessageDto;
import com.lpy.news.entity.UserMessage;
import com.lpy.news.service.SnowService;
import com.lpy.news.service.UserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class UserMessageServiceImpl implements UserMessageService {
    SnowService snowService = new SnowService(1, 1);


    @Autowired
    private UserMessageDao userMessageDao;
    @Override
    public ArrayList<UserMessageDto> queryAllMessage(Long userId) {
        return userMessageDao.queryAllMessage(userId);
    }

    @Override
    public void removeMessage(Long[] ids) {
        for (int i = 0; i <ids.length; i++) {
            Long id = ids[i];
            userMessageDao.removeMessage(id);
        }
    }

    @Override
    public void addUserMessage(Long userId,String message) {
        UserMessage userMessage = new UserMessage();
        userMessage.setId(snowService.getId());
        userMessage.setUserId(userId);
        userMessage.setMessage(message);
        userMessage.setCreateTime(LocalDateTime.now());
        userMessage.setStatus(1);
        userMessageDao.addUserMessage(userMessage);
    }
}
