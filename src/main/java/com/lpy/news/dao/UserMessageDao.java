package com.lpy.news.dao;

import com.lpy.news.dto.UserMessageDto;

public interface UserMessageDao {
    UserMessageDto queryAllMessage(Long userId);

    void removeMessage(Long id);
}
