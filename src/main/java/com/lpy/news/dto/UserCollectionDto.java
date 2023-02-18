package com.lpy.news.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserCollectionDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 新闻id
     */
    private Long newsId;
    /**
     * 关键词
     */
    private String keywds;
    /**
     * 作者
     */
    private String author;
    /**
     * 标题
     */
    private String newsTitle;
    /**
     * 图片
     */
    private String picture1;
    private String picture2;
    private String picture3;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 出版社
     */
    private String publishHouse;
    /**
     * 分类名称
     */
    private String divideName;
}
