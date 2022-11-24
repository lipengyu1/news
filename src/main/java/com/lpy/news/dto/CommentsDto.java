package com.lpy.news.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CommentsDto implements Serializable {

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
}
