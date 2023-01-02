package com.lpy.news.dao;

import com.lpy.news.dto.UserCollectionDto;
import com.lpy.news.entity.UserCollection;

import java.util.List;

public interface UserCollectionDao {
    void addCollection(UserCollectionDto userCollectionDto);

    void delCollection(Long id, Long userId);

    List<UserCollectionDto> queryCollectionPage(int pageNo, int pageSize, Long userId);

    int queryCollectionPageCount(int pageNo, int pageSize, Long userId);
}
