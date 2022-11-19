package com.lpy.news.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;


@Data
public class BasePageResponse<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = 1155528282464285229L;
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