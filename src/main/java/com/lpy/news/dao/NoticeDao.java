package com.lpy.news.dao;

import com.lpy.news.dto.NoticeDto;
import com.lpy.news.entity.Notice;

import java.util.List;

public interface NoticeDao {
    void saveNotice(NoticeDto noticeDto);

    void removeNotice(Long id);

    List<NoticeDto> queryNoticePage(int pageNo, int pageSize, String name);

    int queryNoticeCount(int pageNo, int pageSize, String name);

    void updateNotice(Notice notice);

    NoticeDto selectNoticeById(Long id);
}
