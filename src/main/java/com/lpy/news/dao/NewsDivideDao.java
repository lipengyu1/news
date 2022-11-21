package com.lpy.news.dao;

import com.lpy.news.dto.NewsDto;
import com.lpy.news.entity.NewsDivide;

public interface NewsDivideDao {
    void saveNewsDivide(NewsDivide newsDivide);

    void removeNewsDivide(Long id);

    void updataNewsDivide(NewsDivide newsDivide);

}
