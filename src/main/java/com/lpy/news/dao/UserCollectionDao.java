package com.lpy.news.dao;

import com.lpy.news.entity.UserCollection;

import java.util.List;

public interface UserCollectionDao {
    void addCollection(UserCollection userCollection);

    void delCollection(Long id, Long userId);

    List<UserCollection> queryCollectionPage(int pageNo, int pageSize, Long userId);

    int queryCollectionPageCount(int pageNo, int pageSize, Long userId);

    UserCollection queryUserCollection(Long userId, Long newsId);
}
