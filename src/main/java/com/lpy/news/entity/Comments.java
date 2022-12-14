package com.lpy.news.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comments {
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
     * 创建时间
     */
    private LocalDateTime commentsTime;
    /**
     * 评论内容
     */
    private String commentsContent;
    /**
     * 状态 0未通过 1通过
     */
    private Integer state;
    /**
     * 状态 1正常 0删除
     */
    private Integer status;
}
