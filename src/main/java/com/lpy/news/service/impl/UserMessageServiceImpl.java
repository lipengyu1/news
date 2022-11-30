package com.lpy.news.service.impl;

import com.lpy.news.common.BaseContext;
import com.lpy.news.dao.UserMessageDao;
import com.lpy.news.dto.UserMessageDto;
import com.lpy.news.service.UserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMessageServiceImpl implements UserMessageService {

    @Autowired
    private UserMessageDao userMessageDao;
    @Override
    public UserMessageDto queryAllMessage() {
        Long userId = BaseContext.getCurrentId();
        System.out.println(userId);
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
