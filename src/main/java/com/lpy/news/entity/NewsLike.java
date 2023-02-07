package com.lpy.news.entity;


import lombok.Data;

@Data
public class NewsLike {
    /**
     * 新闻id
     */
    private Long newsId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 点赞状态
     */
    private Integer state;

    public NewsLike(Long newsId, Long userId, Integer state) {
        this.newsId = newsId;
        this.userId = userId;
        this.state = state;
    }
}
