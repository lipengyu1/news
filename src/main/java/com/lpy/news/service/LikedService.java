package com.lpy.news.service;

public interface LikedService {

    /**
     * 将Redis里的点赞数据存入数据库中
     */
    void transLikedFromRedis2DB();
    /**
     * 将Redis中的点赞数量数据存入数据库
     */
    void transLikedCountFromRedis2DB();

}

