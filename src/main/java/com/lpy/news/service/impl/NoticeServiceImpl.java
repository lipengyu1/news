package com.lpy.news.service.impl;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.dao.NoticeDao;
import com.lpy.news.dto.NoticeDto;
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
    @Override
    public void saveNotice(NoticeDto noticeDto) {
        noticeDto.setId(snowService.getId());
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
    public BasePageResponse<NoticeDto> queryNoticePage(int pageNo, int pageSize, String name) {
        int pageNo1 = pageSize * (pageNo - 1);
        List<NoticeDto> queryList = noticeDao.queryNoticePage(pageNo1,pageSize,name);
        ArrayList<NoticeDto> arrayList = new ArrayList<>(queryList);
        int totalCount = noticeDao.queryNoticeCount(pageNo1,pageSize,name);
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
