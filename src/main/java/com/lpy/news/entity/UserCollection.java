package com.lpy.news.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserCollection {
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 新闻id
     */
    private Long newsId;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 状态
     */
    private Integer status;
}
