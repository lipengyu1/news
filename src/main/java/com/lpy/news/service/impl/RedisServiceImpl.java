package com.lpy.news.service.impl;

import com.lpy.news.entity.NewsLike;
import com.lpy.news.service.RedisService;
import com.lpy.news.service.SnowService;
import com.lpy.news.utils.LikedStatusEnum;
import com.lpy.news.utils.RedisKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


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

    @Override
    public void saveUserQuery(String keyWords, Long userId) {
        redisTemplate.opsForList().leftPush(userId, keyWords);
        Long size = redisTemplate.opsForList().size(userId);
        //允许存储100个用户的输入，超过后自动pop
        if (size > 100) {
            redisTemplate.opsForList().rightPop(userId);
        }
    }

    @Override
    public List getUserQuery(Long userId) {
        List list = redisTemplate.opsForList().range(userId,0,-1);
        System.out.println(redisTemplate.opsForList().range(userId,0,-1));
        return list ;
    }
}

