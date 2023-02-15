package com.lpy.news.service;

import com.lpy.news.entity.NewsLike;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    /**
     * 用户搜索记录保存至redis
     */
    void saveUserQuery(String KeyWords,Long userId);
    /**
     * 查询用户输入记录
     */
    List getUserQuery(Long userId);
    /**
     * 保存用户浏览记录
     */
    void saveHistory(Long userId, LocalDateTime date, Long id);
    /**
     *删除用户历史记录
     */
    void delHistory(Long userId, Long newsId);

    /**
     *查询用户历史记录
     */
    Map queryHistory(Long userId);
}
