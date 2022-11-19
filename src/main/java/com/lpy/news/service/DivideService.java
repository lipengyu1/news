package com.lpy.news.service;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.entity.Divide;

public interface DivideService {


    void saveDivide(Divide divide);

    void removeDivide(Long[] ids);

    BasePageResponse<Divide> queryDividePage(int pageNo, int pageSize, String name);

    void updateDivide(Divide divide);

    Divide selectDivideById(Long id);
}
