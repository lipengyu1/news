package com.lpy.news.entity;

import lombok.Data;

@Data
public class NewsDivide {

    private Long id;
    /**
     * 文章id
     */
    private Long newsId;
    /**
     * 分类id
     */
    private Long divideId;
    /**
     * 分类名称
     */
    private String divideName;
    /**
     * 文章标题
     */
    private String newsTitle;
}
