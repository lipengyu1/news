package com.lpy.news.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Notice {
    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 公告内容
     */
    private String content;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 1正常0删除
     */
    private Integer status;
}
