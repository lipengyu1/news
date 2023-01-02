package com.lpy.news.service;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.dto.UserCollectionDto;

public interface UserCollectionService {
    void addCollection(UserCollectionDto userCollectionDto);

    void delCollection(Long[] ids, Long userId);

    BasePageResponse<UserCollectionDto> queryCollectionPage(int pageNo, int pageSize,Long userId);
}
