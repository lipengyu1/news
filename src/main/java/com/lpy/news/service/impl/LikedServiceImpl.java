package com.lpy.news.service.impl;

import com.lpy.news.dao.NewsDao;
import com.lpy.news.dao.NewsLikeDao;
import com.lpy.news.entity.NewsLike;
import com.lpy.news.entity.NewsLikeCount;
import com.lpy.news.entity.NewsLikeCountDB;
import com.lpy.news.entity.NewsLikeDB;
import com.lpy.news.service.LikedService;
import com.lpy.news.service.RedisService;
import com.lpy.news.service.SnowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class LikedServiceImpl implements LikedService {

    @Autowired
    NewsLikeDao newsLikeDao;
    @Autowired
    RedisService redisService;
    SnowService snowService = new SnowService(1, 1);
//  redis点赞详细信息缓存到数据库
    @Override
    @Transactional
    public void transLikedFromRedis2DB() {
        List<NewsLike> list = redisService.getLikedDataFromRedis();
        for (NewsLike like : list) {
            NewsLikeDB newsLikeDB = new NewsLikeDB();
            newsLikeDB.setNewsId(like.getNewsId());
            newsLikeDB.setUserId(like.getUserId());
            newsLikeDB.setId(snowService.getId());
            newsLikeDB.setState(like.getState());
            newsLikeDB.setUpdateTime(LocalDateTime.now());
            newsLikeDao.saveNewsLike(newsLikeDB);
        }
    }
//  redis文章点赞量缓存到数据库
    @Override
    @Transactional
    public void transLikedCountFromRedis2DB() {
        List<NewsLikeCount> list = redisService.getLikedCountFromRedis();
        for (NewsLikeCount dto : list) {
            NewsLikeCountDB newsLikeCountDB = new NewsLikeCountDB();
            newsLikeCountDB.setId(snowService.getId());
            newsLikeCountDB.setNewsId(dto.getNewsId());
            newsLikeCountDB.setLikedCount(dto.getNewsLikeCount());
            newsLikeDao.saveNewsLikeCount(newsLikeCountDB);
        }
    }
}

