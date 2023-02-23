package com.lpy.news.dao;

import com.lpy.news.dto.CommentsDto;

import java.util.List;

public interface CommentsDao {
    void saveComments(CommentsDto commentsDto);

    List<CommentsDto> queryCommentsPage(int pageNo, int pageSize, Integer state);

    int queryCommentsCount(int pageNo, int pageSize, Integer state);

    void removeComments(Long id);

    void updateComments(CommentsDto commentsDto);

    List<CommentsDto> queryCommentsPageByNewsId(int pageNo, int pageSize, Long newsId);

    int qqueryCommentsPageByNewsIdCount(int pageNo, int pageSize, Long newsId);
}
