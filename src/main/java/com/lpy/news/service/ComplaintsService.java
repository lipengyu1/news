package com.lpy.news.service;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.dto.ComplaintsDto;

import java.time.LocalDateTime;

public interface ComplaintsService {
    void saveComplaints(ComplaintsDto complaintsDto);

    void removeComplaints(Long[] ids);

    BasePageResponse<ComplaintsDto> queryComplaintsPage(int pageNo, int pageSize, String complaintsCategory, LocalDateTime complaintsTime);
}
