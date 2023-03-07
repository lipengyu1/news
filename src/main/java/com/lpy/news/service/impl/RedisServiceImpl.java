package com.lpy.news.service.impl;

import com.lpy.news.dto.NewsUserHistoryDto;
import com.lpy.news.entity.NewsLike;
import com.lpy.news.service.RedisService;
import com.lpy.news.utils.LikedStatusEnum;
import com.lpy.news.utils.RedisKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


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
        //设置特殊key
        String key = userId+"keywords";
        redisTemplate.opsForList().leftPush(key, keyWords);
        Long size = redisTemplate.opsForList().size(key);
        //允许存储100个用户的输入，超过后自动pop
        if (size > 100) {
            redisTemplate.opsForList().rightPop(key);
        }
    }

    @Override
    public List getUserQuery(Long userId) {
        String key = userId+"keywords";
        List list = redisTemplate.opsForList().range(key,0,-1);
        return list ;
    }

    @Override
    public void saveHistory(Long userId, LocalDateTime date, Long id) {
        String key = userId+"history";//userId-用户id(key),id-文章id(hashkey)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String datetime = date.format(formatter);//datetime-阅读时间(value)
        redisTemplate.opsForHash().put(key,id,datetime);
    }

    @Override
    public void delHistory(Long userId, Long newsId) {
        String key = userId+"history";
        redisTemplate.opsForHash().delete(key,newsId);
    }

    @Override
    public Map queryHistory(Long userId) {
        String key = userId+"history";
        Map map = redisTemplate.opsForHash().entries(key);
        return map;
    }

    @Override
    public List<Long> getNewsIdlistByUserId(Long user) {
        //获取用户浏览记录
        Map map = queryHistory(user);
        //取出key
        Set keyset = map.keySet();
        List<Long> list = new ArrayList<>();
        ArrayList<NewsUserHistoryDto> arrayList = new ArrayList<>();
        for (Object o :keyset) {
//            NewsUserHistoryDto newsUserHistoryDto = new NewsUserHistoryDto();
            Long newsId = (Long) o;
            list.add(newsId);
        }
        return list;
    }
}

