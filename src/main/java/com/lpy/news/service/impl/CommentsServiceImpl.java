package com.lpy.news.service.impl;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.common.CustomException;
import com.lpy.news.dao.CommentsDao;
import com.lpy.news.dto.CommentsDto;
import com.lpy.news.service.CommentsService;
import com.lpy.news.service.SnowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentsServiceImpl implements CommentsService {

    SnowService snowService = new SnowService(1, 1);

    @Autowired
    CommentsDao commentsDao;
    @Autowired
    private UserMessageServiceImpl userMessageService;

    @Override
    public void saveComments(CommentsDto commentsDto) {
        commentsDto.setId(snowService.getId());
        commentsDto.setCommentsTime(LocalDateTime.now());
        commentsDao.saveComments(commentsDto);
        String comment = commentsDto.getCommentsContent();
        userMessageService.addUserMessage(commentsDto.getUserId(),"您的评论“"+comment+"”已发表，请等待审核，审核通过后方可显示");
    }

    @Override
    public BasePageResponse<CommentsDto> queryCommentsPage(int pageNo, int pageSize, String stateNum) {
        Integer state;
        if (stateNum.equals("")||stateNum.equals(null)){
            state = null;
        }else if (stateNum.equals("拒绝")) {
            state = 0;
        }else if (stateNum.equals("通过")){
            state = 1;
        }else {
            throw new CustomException("审核状态异常");
        }
        int pageNo1 = pageSize * (pageNo - 1);
        List<CommentsDto> queryList = commentsDao.queryCommentsPage(pageNo1,pageSize,state);
        ArrayList<CommentsDto> arrayList = new ArrayList<>(queryList);
        int totalCount = commentsDao.queryCommentsCount(pageNo1,pageSize,state);
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


    @CacheEvict(value = "usermessageCache",allEntries = true)
    @Override
    public void updateComments(CommentsDto commentsDto) {
        Integer state = commentsDto.getState();
        commentsDao.updateComments(commentsDto);
        String comment = commentsDto.getCommentsContent();
        if (state == 1){
            userMessageService.addUserMessage(commentsDto.getUserId(),"您的评论“"+comment+"”审核已通过");
        }else {
            userMessageService.addUserMessage(commentsDto.getUserId(),"您的评论“"+comment+"”审核未通过");
        }
    }

    @Override
    public BasePageResponse<CommentsDto> queryCommentsPageByNewsId(int pageNo, int pageSize, Long newsId) {
        int pageNo1 = pageSize * (pageNo - 1);
        List<CommentsDto> queryList = commentsDao.queryCommentsPageByNewsId(pageNo1,pageSize,newsId);
        ArrayList<CommentsDto> arrayList = new ArrayList<>(queryList);
        int totalCount = commentsDao.qqueryCommentsPageByNewsIdCount(pageNo1,pageSize,newsId);
        BasePageResponse<CommentsDto> basePageResponse = new BasePageResponse<>();
        basePageResponse.setPageNo(pageNo);
        basePageResponse.setPageSize(pageSize);
        basePageResponse.setTotalPage((int)Math.ceil((float)totalCount/pageSize));
        basePageResponse.setResultList(arrayList);
        basePageResponse.setTotalCount(totalCount);
        return basePageResponse;
    }
}
