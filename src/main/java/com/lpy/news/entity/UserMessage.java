package com.lpy.news.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserMessage {
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 消息内容
     */
    private String message;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 状态
     */
    private Integer status;

}
