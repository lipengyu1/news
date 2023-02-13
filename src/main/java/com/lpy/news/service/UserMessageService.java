package com.lpy.news.service;

import com.lpy.news.dto.UserMessageDto;

import java.util.ArrayList;

public interface UserMessageService {
    ArrayList<UserMessageDto> queryAllMessage(Long userId);

    void removeMessage(Long[] ids);

    void addUserMessage(Long userId,String message);
}
