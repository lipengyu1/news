package com.lpy.news.dao;

import com.lpy.news.dto.NewsDto;
import com.lpy.news.entity.Divide;
import com.lpy.news.entity.NewsDivide;

import java.util.List;

public interface NewsDao {
    void saveNews(NewsDto newsDto);


    void removeNews(Long id);

    List<NewsDto> queryNewsPage(int pageNo, int pageSize, String divideName,String author);

    int queryNewsCount(int pageNo1, int pageSize, String divideName,String author);

    void updateNews(NewsDto newsDto);

    NewsDto selectNewsById(Long id);
}
