package com.lpy.news.utils;

public class RedisKeyUtils {

    //保存用户点赞数据的key
    public static final String MAP_USER_LIKED = "MAP_USER_LIKED";
    //保存文章被点赞数量的key
    public static final String MAP_NEWS_LIKED_COUNT = "MAP_NEWS_LIKED_COUNT";

    /**
     * 拼接被点赞的文章id和点赞的用户的id作为key。格式 001::1006
     * @return
     */
    public static String getLikedKey(Long newsId, Long userId){
        StringBuilder builder = new StringBuilder();
        builder.append(newsId);
        builder.append("::");
        builder.append(userId);
        return builder.toString();
    }
}

