package com.lpy.news.service.impl;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.dao.ComplaintsDao;
import com.lpy.news.dto.ComplaintsDto;
import com.lpy.news.service.ComplaintsService;
import com.lpy.news.service.SnowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ComplaintsServiceImpl implements ComplaintsService {
    @Autowired
    ComplaintsDao complaintsDao;
    SnowService snowService = new SnowService(1, 1);

    @Override
    public void saveComplaints(ComplaintsDto complaintsDto) {
        complaintsDto.setId(snowService.getId());
        complaintsDao.saveComplaints(complaintsDto);
    }

    @Override
    public void removeComplaints(Long[] ids) {
        for (int i = 0; i <ids.length; i++) {
            Long id = ids[i];
            complaintsDao.removeComplaints(id);
        }
    }

    @Override
    public BasePageResponse<ComplaintsDto> queryComplaintsPage(int pageNo, int pageSize, String complaintsCategory) {
        int pageNo1 = pageSize * (pageNo - 1);
        List<ComplaintsDto> queryList = complaintsDao.queryComplaintsPage(pageNo1,pageSize,complaintsCategory);
        ArrayList<ComplaintsDto> arrayList = new ArrayList<>(queryList);
        int totalCount = complaintsDao.queryComplaintsCount(pageNo1,pageSize,complaintsCategory);
        BasePageResponse<ComplaintsDto> basePageResponse = new BasePageResponse<>();
        basePageResponse.setPageNo(pageNo);
        basePageResponse.setPageSize(pageSize);
        basePageResponse.setTotalPage((int)Math.ceil((float)totalCount/pageSize));
        basePageResponse.setResultList(arrayList);
        basePageResponse.setTotalCount(totalCount);
        return basePageResponse;
    }
}
