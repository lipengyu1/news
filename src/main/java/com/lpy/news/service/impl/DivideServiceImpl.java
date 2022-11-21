package com.lpy.news.service.impl;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.dao.DivideDao;
import com.lpy.news.entity.Divide;
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
    DivideDao divideDao;
    @Override
    public void saveDivide(Divide divide) {
        divide.setId(snowService.getId());
        divide.setCreateTime(LocalDateTime.now());
        divide.setUpdateTime(LocalDateTime.now());
        divideDao.saveDivide(divide);
    }

    @Override
    public void removeDivide(Long[] ids) {
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
}
