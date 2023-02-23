package com.lpy.news.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

@Data
public class NewsAndRecommendDto implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 新闻详细内容
     */
    private NewsDto newsDto;
    /**
     * 推荐内容
     */
    private ArrayList<NewsUserRecommendDto> recommendList;
}
