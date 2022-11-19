package com.lpy.news.service;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.dto.NoticeDto;
import com.lpy.news.entity.Notice;

public interface NoticeService {
    void saveNotice(NoticeDto noticeDto);

    void removeNotice(Long[] ids);

    BasePageResponse<NoticeDto> queryNoticePage(int pageNo, int pageSize, String name);

    void updateNotice(Notice notice);

    NoticeDto selectNoticeById(Long id);
}
