package com.lpy.news.service.impl;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.common.CustomException;
import com.lpy.news.dao.DivideDao;
import com.lpy.news.dao.EmployeeDao;
import com.lpy.news.dao.PictureDao;
import com.lpy.news.entity.Employee;
import com.lpy.news.entity.Picture;
import com.lpy.news.service.PictureService;
import com.lpy.news.service.SnowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PictureServiceImpl implements PictureService {
    @Autowired
    EmployeeDao employeeDao;
    @Autowired
    DivideDao divideDao;
    @Autowired
    PictureDao pictureDao;
    SnowService snowService = new SnowService(1, 1);
    @Override
    public void savePicture(Picture picture) {
        Employee employeeName = employeeDao.selectEmpNameById(picture.getEmpId());
        String picDivName = picture.getDivideName();
        Long empId = divideDao.selectDivideByName(picDivName);
        if (empId == null){
            throw new CustomException("该分类不存在,请重新选择");
        }
        picture.setEmpName(employeeName.getName());
        picture.setCreateTime(LocalDateTime.now());
        picture.setId(snowService.getId());
        pictureDao.savePicture(picture);
    }

    @Override
    public void removePicture(Long[] ids) {
        for (int i = 0; i <ids.length; i++) {
            Long id = ids[i];
            pictureDao.removeNotice(id);
        }
    }

    public void updateNews(Picture picture) {
        String picDivName = picture.getDivideName();
        try {
            divideDao.selectDivideByName(picDivName);
        } catch (Exception e) {
            throw new CustomException("该分类不存在,请重新选择");
        }
        picture.setCreateTime(LocalDateTime.now());
        pictureDao.updateNews(picture);
    }

    @Override
    public BasePageResponse<Picture> queryPicturePage(int pageNo, int pageSize, String divideName) {
        int pageNo1 = pageSize * (pageNo - 1);
        List<Picture> queryList = pictureDao.queryPicturePage(pageNo1,pageSize,divideName);
        ArrayList<Picture> arrayList = new ArrayList<>(queryList);
        int totalCount = pictureDao.queryPictureCount(pageNo1,pageSize,divideName);
        BasePageResponse<Picture> basePageResponse = new BasePageResponse<>();
        basePageResponse.setPageNo(pageNo);
        basePageResponse.setPageSize(pageSize);
        basePageResponse.setTotalPage((int)Math.ceil((float)totalCount/pageSize));
        basePageResponse.setResultList(arrayList);
        basePageResponse.setTotalCount(totalCount);
        return basePageResponse;
    }

    @Override
    public ArrayList<Picture> showPicturePage(String divideName) {
        ArrayList<Picture> queryList = pictureDao.showPicturePageByDiv(divideName);
        return queryList;
    }
}
