package com.lpy.news.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class NewsAndRecommendDto {
    /**
     * 新闻详细内容
     */
    private NewsDto newsDto;
    /**
     * 推荐内容
     */
    private ArrayList<NewsUserRecommendDto> recommendList;
}
