package com.lpy.news.common;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author ：lpy
 *  @description：分页返回通用对象
 * @date ：Created in 2022/11/6 16:15
 */
@Data
public class BasePageResponse<T extends Serializable> implements Serializable {

    /**
     * 当前页
     */
    private int pageNo;
    /**
     * 每页记录数
     */
    private int pageSize;
    /**
     * 记录总数
     */
    private long totalCount;
    /**
     * 总页数
     */
    private int totalPage;

    /**
     * 返回记录
     */
    private ArrayList<T> resultList;

    public BasePageResponse() {

    }
}