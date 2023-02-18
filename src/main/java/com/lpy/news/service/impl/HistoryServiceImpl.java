package com.lpy.news.service.impl;

import com.lpy.news.dao.DivideDao;
import com.lpy.news.dao.NewsDao;
import com.lpy.news.dao.NewsDivideDao;
import com.lpy.news.dao.NewsLikeDao;
import com.lpy.news.dto.NewsDto;
import com.lpy.news.dto.NewsUserHistoryDto;
import com.lpy.news.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Service
public class HistoryServiceImpl implements HistoryService {
    @Autowired
    private RedisServiceImpl redisService;
    @Autowired
    NewsLikeDao newsLikeDao;
    @Autowired
    NewsDivideDao newsDivideDao;
    @Autowired
    NewsDao newsDao;
    @Override
    public void saveHistory(Long userId, LocalDateTime date, Long id) {
        redisService.saveHistory(userId,date,id);
    }

    @Override
    public void delHistory(Long userId, Long newsId) {
        redisService.delHistory(userId,newsId);
    }

    @Override
    public ArrayList<NewsUserHistoryDto> queryHistory(Long userId) {
        //获取用户浏览记录
        Map map = redisService.queryHistory(userId);
        //取出key
        Set keyset = map.keySet();
        NewsDto newsDto;
        ArrayList<NewsUserHistoryDto> arrayList = new ArrayList<>();
        for (Object o :keyset) {
            NewsUserHistoryDto newsUserHistoryDto = new NewsUserHistoryDto();
            Long newsId1 = (Long) o;
            newsDto = newsDao.selectNewsById(newsId1);
            Integer num = newsLikeDao.selectNewsCountLike(newsId1);
            newsUserHistoryDto.setAuthor(newsDto.getAuthor());
            newsUserHistoryDto.setDivideName(newsDto.getDivideName());
            newsUserHistoryDto.setTitle(newsDto.getTitle());
            newsUserHistoryDto.setId(newsDto.getId());
            newsUserHistoryDto.setKeywds(newsDto.getKeywds());
            newsUserHistoryDto.setPublishHouse(newsDto.getPublishHouse());
            newsUserHistoryDto.setDivideName(newsDto.getDivideName());
            newsUserHistoryDto.setLikeCount(num);
            //根据map中的key获取value阅读时间
            String time = (String) map.get(o);
            newsUserHistoryDto.setReadTime(time);
            arrayList.add(newsUserHistoryDto);
        }
        return arrayList;
    }
}
