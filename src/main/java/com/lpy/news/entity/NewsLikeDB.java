package com.lpy.news.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewsLikeDB {
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
     * 点赞状态
     */
    private Integer state;
    /**
     * 更新入库时间
     */
    private LocalDateTime updateTime;
}
