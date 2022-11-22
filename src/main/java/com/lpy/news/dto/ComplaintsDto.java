package com.lpy.news.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ComplaintsDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 投诉类别
     */
    private String complaintsCategory;
    /**
     * 投诉内容
     */
    private String complaintsContent;
    /**
     * 投诉时间
     */
    private LocalDateTime complaintsTime;
}
