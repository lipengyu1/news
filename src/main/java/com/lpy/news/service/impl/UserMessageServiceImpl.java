package com.lpy.news.service.impl;

import com.lpy.news.dao.UserMessageDao;
import com.lpy.news.dto.UserMessageDto;
import com.lpy.news.service.UserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserMessageServiceImpl implements UserMessageService {

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
}
