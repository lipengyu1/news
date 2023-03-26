package com.lpy.news.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Picture implements Serializable {
    private Long id;
    /**
     * 图片名
     */
    private String img;
    /**
     * 分类名
     */
    private String divideName;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 创建者id
     */
    private Long empId;
    /**
     * 创建者姓名
     */
    private String empName;
    /**
     * 状态
     */
    private Integer status;
}
