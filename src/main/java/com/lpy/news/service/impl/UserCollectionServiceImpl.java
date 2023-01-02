package com.lpy.news.service.impl;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.dao.UserCollectionDao;
import com.lpy.news.dto.ComplaintsDto;
import com.lpy.news.dto.UserCollectionDto;
import com.lpy.news.entity.UserCollection;
import com.lpy.news.service.SnowService;
import com.lpy.news.service.UserCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserCollectionServiceImpl implements UserCollectionService {
    @Autowired
    UserCollectionDao userCollectionDao;
    SnowService snowService = new SnowService(1, 1);
    @Override
    public void addCollection(UserCollectionDto userCollectionDto) {
        userCollectionDto.setCreateTime(LocalDateTime.now());
        userCollectionDto.setId(snowService.getId());
        userCollectionDao.addCollection(userCollectionDto);
    }

    @Override
    public void delCollection(Long[] ids, Long userId) {
        for (int i = 0; i <ids.length; i++) {
            Long id = ids[i];
            userCollectionDao.delCollection(id,userId);
        }
    }

    @Override
    public BasePageResponse<UserCollectionDto> queryCollectionPage(int pageNo, int pageSize,Long userId) {
        int pageNo1 = pageSize * (pageNo - 1);
        List<UserCollectionDto> queryList = userCollectionDao.queryCollectionPage(pageNo1,pageSize,userId);
        ArrayList<UserCollectionDto> arrayList = new ArrayList<>(queryList);
        int totalCount = userCollectionDao.queryCollectionPageCount(pageNo1,pageSize,userId);
        BasePageResponse<UserCollectionDto> basePageResponse = new BasePageResponse<>();
        basePageResponse.setPageNo(pageNo);
        basePageResponse.setPageSize(pageSize);
        basePageResponse.setTotalPage((int)Math.ceil((float)totalCount/pageSize));
        basePageResponse.setResultList(arrayList);
        basePageResponse.setTotalCount(totalCount);
        return basePageResponse;
    }
}
