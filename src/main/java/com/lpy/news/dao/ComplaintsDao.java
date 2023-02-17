package com.lpy.news.dao;

import com.lpy.news.dto.ComplaintsDto;

import java.util.List;

public interface ComplaintsDao {
    void saveComplaints(ComplaintsDto complaintsDto);

    void removeComplaints(Long id);

    List<ComplaintsDto> queryComplaintsPage(int pageNo, int pageSize, String complaintsCategory);

    int queryComplaintsCount(int pageNo, int pageSize, String complaintsCategory);
}
