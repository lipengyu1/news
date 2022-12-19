package com.lpy.news.dao;

import com.lpy.news.dto.UserMessageDto;

import java.util.ArrayList;

public interface UserMessageDao {
    ArrayList<UserMessageDto> queryAllMessage(Long userId);

    void removeMessage(Long id);
}
