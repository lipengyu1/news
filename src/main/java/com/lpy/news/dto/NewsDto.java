package com.lpy.news.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class NewsDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 关键词
     */
    private String keywds;
    /**
     * 作者
     */
    private String author;
    /**
     * 内容
     */
    private String essay;
    /**
     * 图片
     */
    private String picture1;
    private String picture2;
    private String picture3;
    /**
     * 出版社
     */
    private String publishHouse;
    /**
     * 发表时间
     */
    private LocalDateTime publicationTime;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 分类名称
     */
    private String divideName;
}
