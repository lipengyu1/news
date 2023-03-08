package com.lpy.news.service;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.dto.DivideDto;
import com.lpy.news.entity.Divide;

import java.util.ArrayList;

public interface DivideService {


    void saveDivide(Divide divide);

    void removeDivide(Long[] ids);

    BasePageResponse<Divide> queryDividePage(int pageNo, int pageSize, String name);

    void updateDivide(Divide divide);

    Divide selectDivideById(Long id);

    ArrayList<DivideDto> getDivide();
}
