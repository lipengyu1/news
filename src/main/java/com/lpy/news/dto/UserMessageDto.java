package com.lpy.news.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserMessageDto {
    /**
     * 消息内容
     */
    private String message;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
