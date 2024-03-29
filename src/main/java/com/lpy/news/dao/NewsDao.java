package com.lpy.news.dao;

import com.lpy.news.dto.NewsDto;
import com.lpy.news.dto.NewsKeyQueryDto;
import com.lpy.news.dto.NewsUserRecommendDto;

import java.util.ArrayList;
import java.util.List;

public interface NewsDao {
    void saveNews(NewsDto newsDto);


    void removeNews(Long id);

    List<NewsDto> queryNewsPage(int pageNo, int pageSize, String divideName,String author);

    int queryNewsCount(int pageNo1, int pageSize, String divideName,String author);

    void updateNews(NewsDto newsDto);

    NewsDto selectNewsById(Long id);

    ArrayList<NewsKeyQueryDto> queryNews(String keyWords);

    ArrayList<NewsUserRecommendDto> queryNewsRandom();

    //以下为算法使用接口
    List<Long> getAllNewsIdList();

    NewsUserRecommendDto findByTopicIdIn(Long newsId);

}
