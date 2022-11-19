package com.lpy.news.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Divide implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 类名
     */
    private String name;
    /**
     * 顺序
     */
    private Integer sort;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 状态
     */
    private Integer status;
}
