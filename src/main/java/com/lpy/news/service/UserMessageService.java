package com.lpy.news.service;

import com.lpy.news.dto.UserMessageDto;

public interface UserMessageService {
    UserMessageDto queryAllMessage();

    void removeMessage(Long[] ids);
}
