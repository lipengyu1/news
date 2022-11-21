package com.lpy.news.service.impl;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.common.CustomException;
import com.lpy.news.dao.DivideDao;
import com.lpy.news.dao.NewsDao;
import com.lpy.news.dao.NewsDivideDao;
import com.lpy.news.dto.NewsDto;
import com.lpy.news.entity.Divide;
import com.lpy.news.entity.News;
import com.lpy.news.entity.NewsDivide;
import com.lpy.news.service.NewsService;
import com.lpy.news.service.SnowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {
    @Autowired
    DivideDao divideDao;
    @Autowired
    NewsDivideDao newsDivideDao;
    @Autowired
    NewsDao newsDao;
    SnowService snowService = new SnowService(1, 1);
    @Override
    public void saveNews(NewsDto newsDto) {
        newsDto.setId(snowService.getId());
        newsDto.setCreateTime(LocalDateTime.now());
        //设置多对多表
        NewsDivide newsDivide = new NewsDivide();
        newsDivide.setId(snowService.getId());
        newsDivide.setNewsId(newsDto.getId());
        newsDivide.setNewsTitle(newsDto.getTitle());
        newsDivide.setDivideName(newsDto.getDivideName());
        Long divideId = divideDao.selectDivideByName(newsDto.getDivideName());
        newsDivide.setDivideId(divideId);
        if (divideId == null){
            throw new CustomException("该分类不存在,请重新选择");
        }
        newsDivideDao.saveNewsDivide(newsDivide);
        newsDao.saveNews(newsDto);
    }

    @Override
    public void removeNews(Long[] ids) {
        for (int i = 0; i <ids.length; i++) {
            Long id = ids[i];
            newsDao.removeNews(id);
        }
        for (int i = 0; i <ids.length; i++) {
            Long id = ids[i];
            newsDivideDao.removeNewsDivide(id);
        }
    }

    @Override
    public BasePageResponse<NewsDto> queryNewsPage(int pageNo, int pageSize, String divideName,String author) {
        int pageNo1 = pageSize * (pageNo - 1);
        List<NewsDto> queryList = newsDao.queryNewsPage(pageNo1,pageSize,divideName,author);
        ArrayList<NewsDto> arrayList = new ArrayList(queryList);
        int totalCount = newsDao.queryNewsCount(pageNo1,pageSize,divideName,author);
        BasePageResponse<NewsDto> basePageResponse = new BasePageResponse<>();
        basePageResponse.setPageNo(pageNo);
        basePageResponse.setPageSize(pageSize);
        basePageResponse.setTotalPage((int)Math.ceil((float)totalCount/pageSize));
        basePageResponse.setResultList(arrayList);
        basePageResponse.setTotalCount(totalCount);
        return basePageResponse;
    }

    @Override
    public void updateNews(NewsDto newsDto) {
        //更新文章基本信息
        newsDao.updateNews(newsDto);
        //获取更新后文章类别的分类id
        Long divideId = divideDao.selectDivideByName(newsDto.getDivideName());
        if (divideId == null){
            throw new CustomException("该分类不存在,请重新选择");
        }
        NewsDivide newsDivide = new NewsDivide();
        newsDivide.setNewsId(newsDto.getId());
        newsDivide.setDivideId(divideId);
        newsDivide.setDivideName(newsDto.getDivideName());
        newsDivide.setNewsTitle(newsDto.getTitle());
        //更新文章类别对应表
        newsDivideDao.updataNewsDivide(newsDivide);
    }

    @Override
    public NewsDto selectNewsById(Long id) {
        NewsDto newsDto = newsDao.selectNewsById(id);
        return newsDto;
    }
}
