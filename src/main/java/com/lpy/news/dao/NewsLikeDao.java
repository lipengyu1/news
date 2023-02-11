package com.lpy.news.dao;

import com.lpy.news.entity.NewsLikeCountDB;
import com.lpy.news.entity.NewsLikeDB;

import java.util.ArrayList;

public interface NewsLikeDao {
    void saveNewsLike(NewsLikeDB newsLikeDB);

    void delNewsLikeCount();

    void saveNewsLikeCount();

    ArrayList<NewsLikeCountDB> selectNewsId();

    Integer selectNewsCountLike(Long id);
}
