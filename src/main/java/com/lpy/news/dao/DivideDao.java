package com.lpy.news.dao;

import com.lpy.news.dto.DivideDto;
import com.lpy.news.entity.Divide;

import java.util.ArrayList;
import java.util.List;

public interface DivideDao {

    void saveDivide(Divide divide);

    void removeDivide(Long id);

    List<Divide> queryDividePage(int pageNo, int pageSize, String name);

    int queryDivideCount(int pageNo, int pageSize, String name);

    void updateDivide(Divide divide);

    Divide selectDivideById(Long id);

    Long selectDivideByName(String name);

    ArrayList<DivideDto> getDivide();
}
