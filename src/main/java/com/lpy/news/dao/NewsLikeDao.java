package com.lpy.news.dao;

import com.lpy.news.entity.NewsLikeCountDB;
import com.lpy.news.entity.NewsLikeDB;

public interface NewsLikeDao {
    void saveNewsLike(NewsLikeDB newsLikeDB);

    void saveNewsLikeCount(NewsLikeCountDB newsLikeCountDB);
}
