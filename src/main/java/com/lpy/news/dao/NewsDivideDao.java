package com.lpy.news.dao;

import com.lpy.news.dto.NewsDto;
import com.lpy.news.entity.News;
import com.lpy.news.entity.NewsDivide;

import java.util.List;

public interface NewsDivideDao {
    void saveNewsDivide(NewsDivide newsDivide);

    void removeNewsDivide(Long id);

    void updataNewsDivide(NewsDivide newsDivide);

    List<News> selectNewDividesByDivideId(Long id);
}
