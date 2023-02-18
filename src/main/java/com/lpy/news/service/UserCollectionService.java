package com.lpy.news.service;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.dto.UserCollectionDto;
import com.lpy.news.entity.UserCollection;

public interface UserCollectionService {
    void addCollection(UserCollection userCollection);

    void delCollection(Long[] ids, Long userId);

    BasePageResponse<UserCollectionDto> queryCollectionPage(int pageNo, int pageSize,Long userId);
}
