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
     * 标题
     */
    private String newsTitle;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
