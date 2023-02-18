package com.lpy.news.service.impl;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.dao.NewsDao;
import com.lpy.news.dao.NewsLikeDao;
import com.lpy.news.dao.UserCollectionDao;
import com.lpy.news.dto.NewsDto;
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
    NewsDao newsDao;
    @Autowired
    NewsLikeDao newsLikeDao;
    @Autowired
    UserCollectionDao userCollectionDao;
    SnowService snowService = new SnowService(1, 1);
    @Override
    public void addCollection(UserCollection userCollection) {
        userCollection.setCreateTime(LocalDateTime.now());
        userCollection.setId(snowService.getId());
        userCollectionDao.addCollection(userCollection);
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
        List<UserCollection> queryList = userCollectionDao.queryCollectionPage(pageNo1,pageSize,userId);
        List<UserCollectionDto> queryListDetail = new ArrayList<>();
        for (UserCollection userCollection : queryList) {
            Long newsId = userCollection.getNewsId();
            NewsDto newsDto = newsDao.selectNewsById(newsId);
            Integer num = newsLikeDao.selectNewsCountLike(newsId);
            newsDto.setLikeCount(num);
            UserCollectionDto userCollectionDto = new UserCollectionDto();
            userCollectionDto.setId(userCollection.getId());
            userCollectionDto.setCreateTime(userCollection.getCreateTime());
            userCollectionDto.setNewsId(userCollection.getNewsId());
            userCollectionDto.setUserId(userId);
            userCollectionDto.setAuthor(newsDto.getAuthor());
            userCollectionDto.setKeywds(newsDto.getKeywds());
            userCollectionDto.setDivideName(newsDto.getDivideName());
            userCollectionDto.setNewsTitle(newsDto.getTitle());
            userCollectionDto.setPicture1(newsDto.getPicture1());
            userCollectionDto.setPicture2(newsDto.getPicture2());
            userCollectionDto.setPicture3(newsDto.getPicture3());
            userCollectionDto.setPublishHouse(newsDto.getPublishHouse());
            queryListDetail.add(userCollectionDto);
        }
        ArrayList<UserCollectionDto> arrayList = new ArrayList<>(queryListDetail);
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
