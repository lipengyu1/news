package com.lpy.news.entity;

import lombok.Data;

@Data
public class NewsLikeCount {
    /**
     * 新闻id
     */
    private Long newsId;
    /**
     * 点赞数量统计
     */
    private Integer newsLikeCount;

    public NewsLikeCount(Long newsId, Integer newsLikeCount) {
        this.newsId = newsId;
        this.newsLikeCount = newsLikeCount;
    }
}
