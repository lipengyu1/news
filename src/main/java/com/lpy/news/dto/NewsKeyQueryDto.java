package com.lpy.news.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewsKeyQueryDto {
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
     * 分类名称
     */
    private String divideName;
    /**
     * 点赞量
     */
    private Integer likeCount;
}
