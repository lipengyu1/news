package com.lpy.news.service;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.dto.NewsDto;

import java.util.ArrayList;
import java.util.List;

public interface NewsService {
    void saveNews(NewsDto newsDto);

    void removeNews(Long[] ids);

    BasePageResponse<NewsDto> queryNewsPage(int pageNo, int pageSize, String divideName,String author);

    void updateNews(NewsDto newsDto);

    NewsDto selectNewsById(Long id);

    ArrayList<NewsDto> queryHotNews();

    ArrayList<NewsDto> queryNews(String keyWords);

    ArrayList queryHistoryKeyWords();
}
