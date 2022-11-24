package com.lpy.news.service.impl;

import com.lpy.news.common.BaseContext;
import com.lpy.news.common.BasePageResponse;
import com.lpy.news.common.CustomException;
import com.lpy.news.dao.CommentsDao;
import com.lpy.news.dto.CommentsDto;
import com.lpy.news.service.CommentsService;
import com.lpy.news.service.SnowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentsServiceImpl implements CommentsService {

    SnowService snowService = new SnowService(1, 1);

    @Autowired
    private CommentsDao commentsDao;

    @Override
    public void saveComments(CommentsDto commentsDto) {
        commentsDto.setId(snowService.getId());
        //设置用户id，指定当前是哪个用户的评论数据
        Long userId = BaseContext.getCurrentId();
        commentsDto.setCommentsTime(LocalDateTime.now());
        commentsDto.setUserId(userId);
        commentsDao.saveComments(commentsDto);
    }

    @Override
    public BasePageResponse<CommentsDto> queryCommentsPage(int pageNo, int pageSize, String state) {
        Integer stateNum;
        if (state.equals("")||state.equals(null)){
            stateNum = null;
        }else if (state.equals("拒绝")) {
            stateNum = 0;
        }else if (state.equals("通过")){
            stateNum = 1;
        }else {
            throw new CustomException("审核状态异常");
        }
        int pageNo1 = pageSize * (pageNo - 1);
        List<CommentsDto> queryList = commentsDao.queryCommentsPage(pageNo1,pageSize,stateNum);
        ArrayList<CommentsDto> arrayList = new ArrayList<>(queryList);
        int totalCount = commentsDao.queryCommentsCount(pageNo1,pageSize,stateNum);
        BasePageResponse<CommentsDto> basePageResponse = new BasePageResponse<>();
        basePageResponse.setPageNo(pageNo);
        basePageResponse.setPageSize(pageSize);
        basePageResponse.setTotalPage((int)Math.ceil((float)totalCount/pageSize));
        basePageResponse.setResultList(arrayList);
        basePageResponse.setTotalCount(totalCount);
        return basePageResponse;
    }

    @Override
    public void removeComments(Long[] ids) {
        for (int i = 0; i <ids.length; i++) {
            Long id = ids[i];
            commentsDao.removeComments(id);
        }
    }

    @Override
    public void updateComments(CommentsDto commentsDto) {
        commentsDao.updateComments(commentsDto);
    }
}
