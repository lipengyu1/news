package com.lpy.news.entity;

import lombok.Data;

@Data
public class NewsLikeCountDB {
    /**
     * 新闻id
     */
    private Long newsId;
    /**
     * 点赞数量
     */
    private Integer likedCount;
}
