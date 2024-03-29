package com.lpy.news.service.impl;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.common.CustomException;
import com.lpy.news.dao.DivideDao;
import com.lpy.news.dao.NewsDivideDao;
import com.lpy.news.dto.DivideDto;
import com.lpy.news.entity.Divide;
import com.lpy.news.entity.News;
import com.lpy.news.service.DivideService;
import com.lpy.news.service.SnowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DivideServiceImpl implements DivideService {
    SnowService snowService = new SnowService(1, 1);
    @Autowired
    NewsDivideDao newsDivideDao;
    @Autowired
    DivideDao divideDao;
    @Override
    public void saveDivide(Divide divide) {
        if(divideDao.selectDivideByName(divide.getName()) != null){
            throw new CustomException("该分类已存在");
        }
        divide.setId(snowService.getId());
        divide.setCreateTime(LocalDateTime.now());
        divide.setUpdateTime(LocalDateTime.now());
        divideDao.saveDivide(divide);
    }

    @Override
    public void removeDivide(Long[] ids) {
        for (int i = 0; i <ids.length; i++) {
            Long id = ids[i];
            List<News> newsList = newsDivideDao.selectNewDividesByDivideId(id);
            if (!(newsList.isEmpty())){
                throw new CustomException("该分类使用中，无法删除");
            }
        }
        for (int i = 0; i <ids.length; i++) {
            Long id = ids[i];
            divideDao.removeDivide(id);
        }
    }

    @Override
    public BasePageResponse<Divide> queryDividePage(int pageNo, int pageSize, String name) {
        int pageNo1 = pageSize * (pageNo - 1);
        List<Divide> queryList = divideDao.queryDividePage(pageNo1,pageSize,name);
        ArrayList<Divide> arrayList = new ArrayList<>(queryList);
        int totalCount = divideDao.queryDivideCount(pageNo1,pageSize,name);
        BasePageResponse<Divide> basePageResponse = new BasePageResponse<>();
        basePageResponse.setPageNo(pageNo);
        basePageResponse.setPageSize(pageSize);
        basePageResponse.setTotalPage((int)Math.ceil((float)totalCount/pageSize));
        basePageResponse.setResultList(arrayList);
        basePageResponse.setTotalCount(totalCount);
        return basePageResponse;
    }

    @Override
    public void updateDivide(Divide divide) {
        divide.setUpdateTime(LocalDateTime.now());
        divideDao.updateDivide(divide);
    }

    @Override
    public Divide selectDivideById(Long id) {
        return divideDao.selectDivideById(id);
    }

    @Override
    public ArrayList<DivideDto> getDivide() {
        return divideDao.getDivide();
    }
}
