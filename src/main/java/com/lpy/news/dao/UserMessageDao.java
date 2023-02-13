package com.lpy.news.dao;

import com.lpy.news.dto.UserMessageDto;
import com.lpy.news.entity.UserMessage;

import java.util.ArrayList;

public interface UserMessageDao {
    ArrayList<UserMessageDto> queryAllMessage(Long userId);

    void removeMessage(Long id);

    void addUserMessage(UserMessage userMessage);
}
