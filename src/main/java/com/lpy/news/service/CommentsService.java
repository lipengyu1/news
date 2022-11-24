package com.lpy.news.service;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.dto.CommentsDto;

public interface CommentsService {
    void saveComments(CommentsDto commentsDto);

    BasePageResponse<CommentsDto> queryCommentsPage(int pageNo, int pageSize, String state);

    void removeComments(Long[] ids);

    void updateComments(CommentsDto commentsDto);
}
