package com.lpy.news.dao;

import com.lpy.news.dto.CommentsDto;

import java.util.List;

public interface CommentsDao {
    void saveComments(CommentsDto commentsDto);

    List<CommentsDto> queryCommentsPage(int pageNo, int pageSize, Integer stateNum);

    int queryCommentsCount(int pageNo, int pageSize, Integer stateNum);

    void removeComments(Long id);

    void updateComments(CommentsDto commentsDto);
}
