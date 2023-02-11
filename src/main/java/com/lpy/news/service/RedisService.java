package com.lpy.news.service;

import com.lpy.news.entity.NewsLike;
import com.lpy.news.entity.NewsLikeCount;

import java.util.List;

public interface RedisService {
    /**
     * 点赞。状态为1
     */
    void saveLiked2Redis(Long newsId, Long userId);
    /**
     * 取消点赞。将状态改变为0
     */
    void unlikeFromRedis(Long newsId, Long userId);
    /**
     * 该文章的点赞数加1
     */
    void incrementLikedCount(Long newsId);
    /**
     * 该文章的点赞数减1
     */
    void decrementLikedCount(Long newsId);
    /**
     * 获取Redis中存储的所有点赞数据
     * @return
     */
    List<NewsLike> getLikedDataFromRedis();
}
