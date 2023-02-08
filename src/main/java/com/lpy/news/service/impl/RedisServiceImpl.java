package com.lpy.news.service.impl;

import com.lpy.news.entity.NewsLike;
import com.lpy.news.entity.NewsLikeCount;
import com.lpy.news.service.RedisService;
import com.lpy.news.utils.LikedStatusEnum;
import com.lpy.news.utils.RedisKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class RedisServiceImpl implements RedisService {
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public void saveLiked2Redis(Long newsId, Long userId) {
        String key = RedisKeyUtils.getLikedKey(newsId, userId);
        redisTemplate.opsForHash().put(RedisKeyUtils.MAP_USER_LIKED,
                key, LikedStatusEnum.LIKE.getCode());
    }
    @Override
    public void unlikeFromRedis(Long newsId, Long userId) {
        String key = RedisKeyUtils.getLikedKey(newsId, userId);
        redisTemplate.opsForHash().put(RedisKeyUtils.MAP_USER_LIKED,
                key, LikedStatusEnum.UNLIKE.getCode());
    }
    @Transactional
    @Override
    public void incrementLikedCount(Long newsId) {
        redisTemplate.opsForHash().increment(RedisKeyUtils.MAP_NEWS_LIKED_COUNT,newsId,1);
    }
    @Transactional
    @Override
    public void decrementLikedCount(Long newsId) {
        redisTemplate.opsForHash().increment(RedisKeyUtils.MAP_NEWS_LIKED_COUNT,newsId,-1);
    }
//   获取文章点赞详细数据
    @Override
    public List<NewsLike> getLikedDataFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtils.MAP_USER_LIKED, ScanOptions.NONE);
        List<NewsLike> list = new ArrayList<>();
        while (cursor.hasNext()){
            Map.Entry<Object, Object> entry = cursor.next();
            String key = (String) entry.getKey();
            //分离出 newsId，userId
            String[] split = key.split("::");
            String news = split[0];
            Long newsId = Long.valueOf(news);
            String user = split[1];
            Long userId = Long.valueOf(user);
            Integer state = (Integer) entry.getValue();
            //组装成 UserLike 对象
            NewsLike newsLike = new NewsLike(newsId, userId, state);
            list.add(newsLike);
            //存到 list 后从 Redis 中删除
            redisTemplate.opsForHash().delete(RedisKeyUtils.MAP_USER_LIKED, key);
        }
        return list;
    }
//    获取文章点赞量
    @Override
    public List<NewsLikeCount> getLikedCountFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtils.MAP_NEWS_LIKED_COUNT,  ScanOptions.NONE);
        List<NewsLikeCount> list = new ArrayList<>();
        while (cursor.hasNext()){
            Map.Entry<Object, Object> map = cursor.next();
            //将点赞数量存储在 newslikecount

            NewsLikeCount dto = new NewsLikeCount((Long) map.getKey(),(Integer)map.getValue());
            list.add(dto);
            //从Redis中删除这条记录
            redisTemplate.opsForHash().delete(RedisKeyUtils.MAP_NEWS_LIKED_COUNT, (Long) map.getKey());
        }
        return list;
    }
}

