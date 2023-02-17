package com.lpy.news.dao;

import com.lpy.news.dto.UserCollectionDto;

import java.util.List;

public interface UserCollectionDao {
    void addCollection(UserCollectionDto userCollectionDto);

    void delCollection(Long id, Long userId);

    List<UserCollectionDto> queryCollectionPage(int pageNo, int pageSize, Long userId);

    int queryCollectionPageCount(int pageNo, int pageSize, Long userId);
}
