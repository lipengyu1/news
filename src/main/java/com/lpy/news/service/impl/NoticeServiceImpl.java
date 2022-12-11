package com.lpy.news.service.impl;

import com.lpy.news.common.BaseContext;
import com.lpy.news.common.BasePageResponse;
import com.lpy.news.common.CustomException;
import com.lpy.news.dao.EmployeeDao;
import com.lpy.news.dao.NoticeDao;
import com.lpy.news.dto.NoticeDto;
import com.lpy.news.entity.Employee;
import com.lpy.news.entity.Notice;
import com.lpy.news.service.NoticeService;
import com.lpy.news.service.SnowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {
    SnowService snowService = new SnowService(1, 1);
    @Autowired
    NoticeDao noticeDao;
    @Autowired
    EmployeeDao employeeDao;
    @Override
    public void saveNotice(NoticeDto noticeDto) {
        noticeDto.setId(snowService.getId());
        Long userId = BaseContext.getCurrentId();
        noticeDto.setUserId(userId);
        Employee createEmp = null;
        try {
            createEmp = employeeDao.selectEmpNameById(userId);
        } catch (Exception e) {
            throw new CustomException("用户id错误");
        }
        try {
            noticeDto.setCreatePerson(createEmp.getName());
        } catch (Exception e) {
            throw new CustomException("用户名错误");
        }
        noticeDto.setCreateTime(LocalDateTime.now());
        noticeDto.setUpdateTime(LocalDateTime.now());
        noticeDao.saveNotice(noticeDto);
    }

    @Override
    public void removeNotice(Long[] ids) {
        for (int i = 0; i <ids.length; i++) {
            Long id = ids[i];
            noticeDao.removeNotice(id);
        }
    }

    @Override
    public BasePageResponse<NoticeDto> queryNoticePage(int pageNo, int pageSize, String createPerson) {
        int pageNo1 = pageSize * (pageNo - 1);
        List<NoticeDto> queryList = noticeDao.queryNoticePage(pageNo1,pageSize,createPerson);
        ArrayList<NoticeDto> arrayList = new ArrayList<>(queryList);
        int totalCount = noticeDao.queryNoticeCount(pageNo1,pageSize,createPerson);
        BasePageResponse<NoticeDto> basePageResponse = new BasePageResponse<>();
        basePageResponse.setPageNo(pageNo);
        basePageResponse.setPageSize(pageSize);
        basePageResponse.setTotalPage((int)Math.ceil((float)totalCount/pageSize));
        basePageResponse.setResultList(arrayList);
        basePageResponse.setTotalCount(totalCount);
        return basePageResponse;
    }

    @Override
    public void updateNotice(Notice notice) {
        notice.setUpdateTime(LocalDateTime.now());
        noticeDao.updateNotice(notice);
    }

    @Override
    public NoticeDto selectNoticeById(Long id) {
        return noticeDao.selectNoticeById(id);
    }
}
